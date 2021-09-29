package com.example.digging.adapter.apple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppleServiceImpl {

    @Autowired
    AppleUtil appleUtils;


    @Transactional
    public String getAppleSUBIdentity(String id_token) {

        try {
            if (appleUtils.verifyIdentityToken(id_token)) {
                String returnString = appleUtils.decodeUid(id_token);
                return returnString;
            }
            else {
                return "not valid id_token";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "Null Point";
        } catch (Exception e) {
            e. printStackTrace();
            return "Exception";
        }

    }

    @Transactional
    public String getAppleEmailIdentity(String id_token) {

        try {
            if (appleUtils.verifyIdentityToken(id_token)) {
                String returnString = appleUtils.decodeEmail(id_token);
                return returnString;
            }
            else {
                return "not valid id_token";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "Null Point";
        } catch (Exception e) {
            e. printStackTrace();
            return "Exception";
        }

    }


}