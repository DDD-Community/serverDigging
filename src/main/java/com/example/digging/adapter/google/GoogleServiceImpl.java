package com.example.digging.adapter.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleServiceImpl {

    @Transactional
    public String verifyAndGetUid(String id_token) {
        HttpTransport transport = new NetHttpTransport();
        GsonFactory gsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList("${google.client.id}"))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(id_token);

            GoogleIdToken.Payload payload = idToken.getPayload();
            String uid = payload.getSubject();

            return uid;
        } catch (GeneralSecurityException e) {
            log.warn(e.getLocalizedMessage());
        } catch (IOException e) {
            log.warn(e.getLocalizedMessage());
        }
        return null;
    }

    @Transactional
    public String verifyAndGetUsername(String id_token) {
        HttpTransport transport = new NetHttpTransport();
        GsonFactory gsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList("${google.client.id}"))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(id_token);

            GoogleIdToken.Payload payload = idToken.getPayload();
            String username = (String) payload.get("name");

            return username;
        } catch (GeneralSecurityException e) {
            log.warn(e.getLocalizedMessage());
        } catch (IOException e) {
            log.warn(e.getLocalizedMessage());
        }
        return null;
    }

    @Transactional
    public String verifyAndGetEmail(String id_token) {
        HttpTransport transport = new NetHttpTransport();
        GsonFactory gsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                .setAudience(Collections.singletonList("${google.client.id}"))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(id_token);

            GoogleIdToken.Payload payload = idToken.getPayload();
            String useremail = payload.getEmail();

            return useremail;
        } catch (GeneralSecurityException e) {
            log.warn(e.getLocalizedMessage());
        } catch (IOException e) {
            log.warn(e.getLocalizedMessage());
        }
        return null;
    }
}
