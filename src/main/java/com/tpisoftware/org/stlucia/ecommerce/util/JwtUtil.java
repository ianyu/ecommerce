package com.tpisoftware.org.stlucia.ecommerce.util;

import com.tpisoftware.org.stlucia.ecommerce.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private String KEY = "__your_super_secret_key_32_bytes__";
    private final SecretKey SECRET_KEY;

    public JwtUtil() {
        if (KEY.length() < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 bytes long");
        }
        this.SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + 3600000); // 1小時有效

        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public String extractUsername(String token) {
        String username = parseClaims(token).getSubject();
        return username;
    }

    public Long extractUserId(String token) {
        Object id = parseClaims(token).get("id");
        Long userId = Long.parseLong(id.toString());
        return userId;
    }

    private Claims parseClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

}