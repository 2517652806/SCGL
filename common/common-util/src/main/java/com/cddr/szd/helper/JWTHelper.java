package com.cddr.szd.helper;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cddr.szd.model.User;

import java.util.Date;

/**
 * JWT工具包
 * @author ZY
 */
public class JWTHelper {
//    private static long tokenExpiration = 365*24*60*60*1000;
    private static long tokenExpiration = 3;
    private static String tokenSignKey = "szdKey";//密钥

    public static String createToken(User user){
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getUserName())
                .withClaim("type", user.getType())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12 * 1000))
                .sign(Algorithm.HMAC256(tokenSignKey));
    }
    public static User getUserInfo(String token){
        DecodedJWT jwt = JWT.decode(token);
        User user = new User();
        user.setId(jwt.getClaim("id").asInt());
        user.setUserName(jwt.getClaim("username").asString());
        user.setType(jwt.getClaim("type").asInt());
        return user;
    }
    public static Integer getUserId(String token){
        return JWT.decode(token)
                .getClaim("id").asInt();
    }

    //接收token,验证token,并返回业务数据
    public static DecodedJWT parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(tokenSignKey))
                .build()
                .verify(token);
    }
}
