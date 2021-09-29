package com.example.digging.adapter.google;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleServiceImpl {


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

            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

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
    public String verifyAndGetEmail(String AccessToken) {
        //String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+access_Token;
        String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+AccessToken;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + AccessToken);
            int responseCode = conn.getResponseCode();

            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

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
