package com.example.BeautServices.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    //Extract Claim from resolver
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    //Check expiration
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Extract expiration from the token
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //Extract username from the token
    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    //Validate the token expiration
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    //Extract claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(signInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Generate token
    @Override
    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(signInKey())
                .compact();
    }

    //Generate refresh token
    @Override
    public String generateRefToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts
                .builder().claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60480000))
                .signWith(signInKey())
                .compact();

    }

    private SecretKey signInKey(){
        String secretKey = "lkjhgfdstyuiopoiuytrewertyuioppoiuytrewwiuytrewqkjhgfdsjhgfdoiuytrewkjhgfd";
        byte [] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
