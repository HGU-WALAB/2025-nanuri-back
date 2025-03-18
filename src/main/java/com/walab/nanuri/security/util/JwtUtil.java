package com.walab.nanuri.security.util;

import com.walab.nanuri.commons.exception.DoNotLoginException;
import com.walab.nanuri.commons.exception.WrongTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
//    private static final long EXPIRE_TIME_MS = 1000 * 60 * 60 * 2; // 2 hours
    private static final long EXPIRE_TIME_MS = 1000 * 60 * 60 * 24 * 7;  // 7 days - 개발 환경 편의성을 위해 임시로 수정
    private static final long REFRESH_EXPIRE_TIME_MS = 1000 * 60 * 60 * 24 * 7;  // 7 days

    public static Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    // JWT access token 발급
    public static String createToken(String uniqueId, String name, String department, Key secretKey) {
        Claims claims = Jwts.claims();
        claims.put("uniqueId", uniqueId);
        claims.put("name", name);
        claims.put("department", department);
        log.info("Creating JWT access token for user: {}", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT refresh token 발급
    public static String createRefreshToken(String uniqueId, String name, Key secretKey) {
        Claims claims = Jwts.claims();
        claims.put("uniqueId", uniqueId);
        claims.put("name", name);
        log.info("Creating JWT refresh token for user: {}", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static String getUserId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("uniqueId", String.class);
    }

    private static Claims extractClaims(String token, String secretKey) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new WrongTokenException("만료된 토큰입니다.");
        } catch (Exception e) {
            throw new WrongTokenException("유효하지 않은 토큰입니다.");
        }
    }

    //User의 UniqueId 반환
    public static String getUserUniqueId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getPrincipal().equals("anonymousUser")){
            throw new DoNotLoginException();
        }
        return authentication.getName();
    }
}