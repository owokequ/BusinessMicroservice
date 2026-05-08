package ru.artem.auth_service.config.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.dto.response.JwtClaims;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secretAccess}")
    private String secretKeyAccess;

    @Value("${jwt.expirationAccess}")
    private Long timeLifeAccess;

    @Value("${jwt.secretRefresh}")
    private String secretKeyRefresh;

    @Value("${jwt.expirationRefresh}")
    private Long timeLifeRefresh;

    public String generateAccessToken(JwtClaims jwtClaims) {

        Map<String, Object> claims = new HashMap<>();
        List<String> roles = jwtClaims.roles();
        claims.put("roles", roles);

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(jwtClaims.username())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeLifeAccess))
                .signWith(getSignInKeyAccess())
                .compact();
    }

    public String generateRefreshToken(JwtClaims jwtClaims) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = jwtClaims.roles();
        claims.put("roles", roles);
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(jwtClaims.username())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeLifeRefresh))
                .signWith(getSignInKeyRefresh())
                .compact();
    }

    public Claims validationRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKeyRefresh())
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return getAllClaims(token).get("roles", List.class);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKeyAccess())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKeyAccess() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyAccess);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignInKeyRefresh() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyRefresh);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
