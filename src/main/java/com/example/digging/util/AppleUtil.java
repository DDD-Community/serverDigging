package com.example.digging.util;

import com.example.digging.adapter.apple.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Base64;
import java.util.Base64.Decoder;


import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import com.google.gson.Gson;

import java.util.Date;

@Component
public class AppleUtil {

    public boolean verifyIdentityToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();

            // EXP
            Date currentTime = new Date(System.currentTimeMillis());
            if (!currentTime.before(payload.getExpirationTime())) {
                return false;
            }

            // NONCE(Test value), ISS, AUD
//            if (!"20B20D-0S8-1K8".equals(payload.getClaim("nonce")) || !ISS.equals(payload.getIssuer()) || !AUD.equals(payload.getAudience().get(0))) {
//                return false;
//            }

            // RSA
            if (verifyPublicKey(signedJWT)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean verifyPublicKey(SignedJWT signedJWT) {

        try {
            String publicKeys = HttpClientUtils.doGet("https://appleid.apple.com/auth/keys");
            ObjectMapper objectMapper = new ObjectMapper();
            AppleKeys keys = objectMapper.readValue(publicKeys, AppleKeys.class);
            for (AppleKey key : keys.getKeys()) {
                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);

                if (signedJWT.verify(verifier)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String decode(String token) {

        String[] splitToken = token.split("\\.");
        Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(splitToken[1]);

        String decodedString = null;
        try {
            decodedString = new String(decodedBytes, "UTF-8");

            Gson gsons = new Gson();

            TokenResponse tokenResponse = gsons.fromJson(decodedString, TokenResponse.class);

            return tokenResponse.getSub();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return decodedString;
        }

    }
}
