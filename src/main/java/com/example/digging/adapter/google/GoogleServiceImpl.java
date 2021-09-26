package com.example.digging.adapter.google;

import com.example.digging.adapter.apple.AppleUtil;

import com.example.digging.adapter.apple.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleServiceImpl {

    @Autowired
    AppleUtil appleUtils;

    private static final String GOOGLE_SNS_CLIENT_ID = "${google.client.id}";

    @Transactional
    public String verifyAndGetUid(String AccessToken) {
        //String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+access_Token;
        String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+AccessToken;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + AccessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : "+responseCode);
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
//                JsonParser parser = new JsonParser();

                System.out.println("result : "+result);
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                String uid = element.getAsJsonObject().get("id").getAsString();

                return uid;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Transactional
    public String verifyAndGetUsername(String AccessToken) {
        //String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+access_Token;
        String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+AccessToken;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + AccessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : "+responseCode);
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
//                JsonParser parser = new JsonParser();

                System.out.println("result : "+result);
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                String name = element.getAsJsonObject().get("name").getAsString();

                return name;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Transactional
    public String verifyAndGetEmail(String AccessToken) {
        //String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+access_Token;
        String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+AccessToken;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + AccessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : "+responseCode);
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }
//                JsonParser parser = new JsonParser();

                System.out.println("result : "+result);
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                String email = element.getAsJsonObject().get("email").getAsString();

                return email;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
