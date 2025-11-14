package com.socialmedia.feedservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.socialmedia.feedservice.exception.UnauthorizedException;

import java.security.Key;
import java.util.Date;

@Service
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String validateTokenAndGetUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            if (claims != null && !isTokenExpired(token)) {
                return claims.getSubject();
            }
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        throw new UnauthorizedException("Invalid token");
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
