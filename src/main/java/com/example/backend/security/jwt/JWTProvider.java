package com.example.backend.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt_expiration}")
    private int jwt_expiration;
    @Value("${jwt_secret}")
    private String jwt_secret;

    public String generateAccessToken(UserDetails userDetails){
        Date today = new Date();
        // tao token
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(today)
                .setExpiration(new Date(today.getTime()+jwt_expiration))
                .signWith(SignatureAlgorithm.HS384,jwt_secret)
                .compact();
    }

    //  check token
    public boolean validateAccessToken(String token){
        try {
            Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException ex){
            log.error("Chuoi jwt token het hieu luc");
        }catch (UnsupportedJwtException ex){
            log.error("API server khong ho tro jwt");
        }catch (MalformedJwtException ex){
            log.error("Chuoi jwt khong dung dinh dang");
        }catch (SignatureException ex){
            log.error("Chuoi jwt ma hoa khong dung");
        }catch (IllegalArgumentException ex){
            log.error("Tham so truyen toi khong ton tai");
        }
        return false;
    }

    // lay username tu chuoi token
    public String getUserNameFromToken(String token){
        return Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token).getBody().getSubject();
    }
}
