package com.example.digging.adapter.apple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppleServiceImpl {

    @Autowired
    AppleUtil appleUtils;

    /**
     * 유효한 id_token인 경우 client_secret 생성
     *
     * @param id_token
     * @return
     */
    @Transactional
    public String getAppleSUBIdentity(String id_token) {

        try {
            if (appleUtils.verifyIdentityToken(id_token)) {
                String returnString = appleUtils.decode(id_token);
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