package com.example.digging.adapter.apple;

import com.example.digging.util.AppleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public String getAppleSUBIdentity(String id_token) {

        if (appleUtils.verifyIdentityToken(id_token)) {
            return "valid id_token";
        }

        return null;
    }


}