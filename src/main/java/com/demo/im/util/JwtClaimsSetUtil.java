package com.demo.im.util;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtClaimsSetUtil {

    public JWTClaimsSet buildJWTClaimsSet(String userId,Integer terminal) {
        Calendar signTime = Calendar.getInstance();
        Date signTimeTime = signTime.getTime();
        signTime.add(Calendar.MINUTE, 60*24);
        Date expireTime = signTime.getTime();
        return new JWTClaimsSet.Builder()
                .issuer("http://192.168.1.100:41000")
                .subject(userId)
                .audience(Arrays.asList("https://app-one.com", "https://app-two.com"))
                .expirationTime(expireTime)
                .notBeforeTime(signTimeTime)
                .issueTime(signTimeTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", "read write")
                .claim("terminal",terminal)
                .build();
    }
}
