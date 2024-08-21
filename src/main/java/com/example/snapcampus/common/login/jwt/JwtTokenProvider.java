package com.example.snapcampus.common.login.jwt;

import com.example.snapcampus.common.login.TokenInfo;
import com.example.snapcampus.dto.security.SnapPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access_secret}")
    private String accessSecretKey;

    @Value("${jwt.refresh_secret}")
    private String refreshSecretKey;

    private Key accessKey;
    private Key refreshKey;

    public JwtTokenProvider() {
        // Empty constructor
    }

    @PostConstruct
    public void init() {
        accessKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }

    public TokenInfo createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date accessExpiration = new Date(now.getTime() + 1000 * 60 * 30); // 30 minutes
        Date refreshExpiration = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 30); // 30 days

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(refreshExpiration)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        return new TokenInfo(authentication.getName(), "Bearer", accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getAccessTokenClaims(token);
        String auth = claims.get("auth", String.class);

        Collection<GrantedAuthority> authorities = Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new SnapPrincipal(claims.getSubject(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public TokenInfo validateRefreshTokenAndCreateToken(String refreshToken) {
        try {
            Claims refreshClaims = getRefreshTokenClaims(refreshToken);
            Date now = new Date();

            String newAccessToken = Jwts.builder()
                    .setSubject(refreshClaims.getSubject())
                    .claim("auth", refreshClaims.get("auth"))
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + 1000 * 60 * 30)) // 30분
                    .signWith(accessKey, SignatureAlgorithm.HS256)
                    .compact();

            String newRefreshToken = Jwts.builder()
                    .setSubject(refreshClaims.getSubject())
                    .claim("auth", refreshClaims.get("auth"))
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + 1000 * 60 * 60 * 24 * 30)) // 30일
                    .signWith(refreshKey, SignatureAlgorithm.HS256)
                    .compact();

            return new TokenInfo(refreshClaims.getSubject(), "Bearer", newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Error processing refresh token", e);
        }
    }

    public boolean validateAccessTokenForFilter(String token) {
        try {
            getAccessTokenClaims(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private Claims getAccessTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getRefreshTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
