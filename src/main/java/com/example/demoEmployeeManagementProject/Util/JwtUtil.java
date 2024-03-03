package com.example.demoEmployeeManagementProject.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "I am Akash";

    // its calculation of minutes in seconds (60 seconds= 1 minute)
    //    private static final int TOKEN_VALIDITY = 60;
    private static final int TOKEN_VALIDITY = 240;

    public String getEmpEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String empEmailId = getEmpEmailFromToken(token);
        return (empEmailId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //its created for generate token body
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                /* these below two lines are same */
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000)) //its calculate 1 minute only
                //.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1))) //HERE 1 MEANS 1MINUTE
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
