package com.raki.api_gateway.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey".getBytes());
    public void validateToken(String token){
        System.out.println("VALIDATING TOKEN: " + token);
        Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();

        System.out.println("GENERATED TOKEN: " + token);
        System.out.println("USERNAME: " + username);

        return token;
    }
}
