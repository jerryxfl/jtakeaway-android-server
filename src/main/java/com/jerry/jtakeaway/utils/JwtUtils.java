package com.jerry.jtakeaway.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.shiro.codec.Base64;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JwtUtils {
    @Resource
    private RedisUtils redisUtils;

    private final String secret = "32125asdafsdfsdfasdads13asd46asdas";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    /**
     * token存活时间，2小时
     */
    private final long liveMills = 3600 * 2 * 1000;

    /**
     * 获取secret
     *
     * @return
     */
    public SecretKey obtainKey() {
        //对key进行解码
        byte[] secretBytes = secret.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(secretBytes, 0, secretBytes.length, "AES");
        return keySpec;
    }

    /**
     * 获取token
     *
     * @param subject JWT所面向的下发者，
     * @return
     */
    public String createJWT(String subject) {
        //加密模式
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);  //iat token签发时间
        SecretKey secretKey = obtainKey();
        //jti表示该token的唯一id,不推荐使用相同值|isa 下发时间
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("jti-xp")
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, secretKey);
        if (liveMills > 0) {
            long expMills = currentTimeMillis + liveMills;
            Date expDate = new Date(expMills);  //失效时间
            jwtBuilder.setExpiration(expDate);
        }

        String token = jwtBuilder.compact();


        JSONObject json = JSONObject.parseObject(subject);
        String account = json.getString("account");
        redisUtils.set(account,token,liveMills);

        return token;
    }

    /**
     * 解密
     *
     * @param jwt
     */
    public Claims parseJWT(String jwt) {
        SecretKey key = obtainKey();
        Claims body = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();  // Claims [kleɪmz] 声明
        return body;
    }


    public void refreshToken(String token) {
        try {
            final Claims claims = parseJWT(token);

            String subject = claims.getSubject();
            JSONObject json = JSONObject.parseObject(subject);
            String account = json.getString("account");
            redisUtils.set(account,token,liveMills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("account", "xiaopang");
//        jsonObject.put("host", "127.0.0.1");
//        String token = JwtUtils.createJWT(jsonObject.toJSONString());
//        System.out.println(token);
//        //解析token
//        Claims claims = parseJWT(token);
//
//        Date d1 = claims.getIssuedAt();
//        Date d2 = claims.getExpiration();
//
//        String subject = claims.getSubject();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//        System.out.println(subject);
//        System.out.println("令牌签发时间：" + sdf.format(d1));
//        System.out.println("令牌过期时间：" + sdf.format(d2));
//        refreshToken(token);
//
//        //base64 payload解析
////        String payload="eyJqdGkiOiJqdGkteHAiLCJpYXQiOjE1NjM5NDcxMTAsInN1YiI6IntcImhvc3RcIjpcIjEyNy4wLjAuMVwiLFwidXNlcm5hbWVcIjpcInhpYW9wYW5nXCJ9IiwiZXhwIjoxNTYzOTU0MzEwfQ";
////        org.apache.shiro.codec.Base64
////        System.out.println("payload的base64解密："+new String(Base64.decode(payload)));
////        base64 header解析
////        String header="eyJhbGciOiJIUzI1NiJ9";
////        System.out.println("header的base64解密："+new String(Base64.decode(header)));
//    }
}