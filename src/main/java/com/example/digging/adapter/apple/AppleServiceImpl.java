package com.example.digging.adapter.apple;

import com.example.digging.util.AppleUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String getAppleSUBIdentity(String id_token) {


//        try {
//            Claims claims = JwtTokenService.getClaims(id_token);
//            String value = (String)claims.get("sub");
//            return value;
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            return "Null Point";
//        } catch (Exception e) {
//            e. printStackTrace();
//            return "Exception";
//        }

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