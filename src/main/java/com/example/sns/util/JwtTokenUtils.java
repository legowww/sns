package com.example.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    //토큰에 유저네임을 넣어서 유저가 어떤 유저인지 식별 가능
    //key 는 유저네임을 넣은 값을 암호화할때 사용
    public static String generateToken(String userName, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256) //256바이트 사용하는 해시알고리즘
                .compact(); //토큰 만드는 메서드
    }

    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
