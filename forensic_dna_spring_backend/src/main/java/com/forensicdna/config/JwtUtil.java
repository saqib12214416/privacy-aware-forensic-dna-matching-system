package com.forensicdna.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {


    @Value("${jwt.secret}")
    private String SECRET_KEY;


    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;



    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(
                    SignatureAlgorithm.HS384,
                    SECRET_KEY
                )
                .compact();
    }


    public String extractEmail(String token){

        return extractClaims(token).getSubject();
    }


    public String extractRole(String token){

        return extractClaims(token)
                .get("role", String.class);
    }


    public boolean isTokenValid(String token){

        try{

            return extractClaims(token)
                    .getExpiration()
                    .after(new Date());

        }catch(Exception e){

            return false;
        }
    }


    private Claims extractClaims(String token){

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}