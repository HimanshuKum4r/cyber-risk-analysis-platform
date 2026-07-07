package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);

    }
    @Override
    public Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }
    @Override
    public String extractUsername(String token) {

        return extractAllClaims(token)

                .getSubject();

    }



    private Instant extractExpiration(String token) {

        return extractAllClaims(token)

                .getExpiration()

                .toInstant();

    }

    @Override
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)

                .isBefore(Instant.now());

    }
    @Override
    public String generateAccessToken(CustomUserDetails user) {

        return generateAccessToken(new HashMap<>(), user);

    }
    @Override
    public String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomUserDetails user
    ) {

        return Jwts.builder()

                .claims(extraClaims)

                .subject(user.getUsername())

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessTokenExpiration
                        )
                )

                .signWith(getSigningKey())

                .compact();

    }
    @Override
    public boolean isTokenValid(
            String token,
            CustomUserDetails user
    ) {

        String username = extractUsername(token);

        return username.equals(user.getUsername())

                &&

                !isTokenExpired(token);

    }




}
