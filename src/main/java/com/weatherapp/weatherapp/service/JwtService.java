package com.weatherapp.weatherapp.service;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;

    // Constructor
    public JwtService() {
        // For production, read from env/application.properties instead
        String secret = "l7o6dF8Zq2B8q2QhP7o5vV9jH1l9N3bC0QyWvE9l8sZbU6dQk1jH9rT5pQ2mX7nC"; // must be >= 32 chars
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getJWTToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey)
                .compact();
    }

    // Get ID
    public String getUserId(String token) {
        Claims data = getTokenData(token);
        return (data != null) ? data.get("id", String.class) : null;
    }

    // Get Email
    public String getEmail(String token) {
        Claims data = getTokenData(token);
        return (data != null) ? data.get("email", String.class) : null;
    }

    public Object getFieldFromToken(String token, String key) {
        Claims data = getTokenData(token);
        return (data != null) ? data.get(key) : null;
    }

    private Claims getTokenData(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
