package com.ntp.be.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenHelper {
    @Value("${jwt.auth.app}")
    private String appName;

    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    @Value("${jwt.auth.expires_in}")
    private int expiresIn;

    //👉 Tạo ra một JWT mới khi user đăng nhập thành công.
    public String generateToken(String username) {
        return Jwts.builder()
                .issuer(appName)
                .subject(username)
                .issuedAt(new Date())
                .expiration(generateExpirationDate())
                .signWith(getSigningKey())
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 1000L);
    }

    private Key getSigningKey() {
        byte[] keysBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keysBytes);
    }

    //👉 Lấy JWT từ request
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    //👉 Kiểm tra token có hợp lệ không:
    // Username trong token trùng với username trong database?
    // Token đã hết hạn chưa?
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //👉 Lấy username từ token.
    public String getUserNameFromToken(String authToken) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(authToken);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    private boolean isTokenExpired(String token) {
        Date expireDate = getExpirationDate(token);
        return expireDate.before(new Date());
    }

    private Date getExpirationDate(String token) {
        Date expireDate;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expireDate = claims.getExpiration();
        } catch (Exception e) {
            expireDate = null;
        }

        return expireDate;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}