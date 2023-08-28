package com.example.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    //EN ESTA CLASE SE PASA UNA FIRMA Q YA VIENE ENCRIPTADA , QUE SE SACÓ DE UNA PAG DE ENCRIPTACIONES,  Y SE VA A ENCRIPTAR AUN MÁS.

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    //crea token de acceso
    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)                                                  //la firma q ya tenemos encriptada, la encriptamos aun más con el algoritmo Hs256.
                .compact();
    }

    //obtiene firma del token
    public Key getSignatureKey(){
        byte[]keyBytes = Decoders.BASE64.decode(secretKey);                                                             //decodifica
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //validar el token de acceso cuando un usuario se intenta autenticar en la app
    public  Boolean IsTokenValid(String token){
        try{
            Jwts.parserBuilder()                                                                                        //parserBuilder() : lee el token.
                    .setSigningKey(getSignatureKey())                                                                   //se envia la firma
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch(Exception e){
            log.error("Token invalido: ".concat(e.getMessage()));
            return false;
        }
    }

    //permite obtener las caracteristicas (los claims del token) del token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()                                                                                     //parserBuilder() : lee el token.
                .setSigningKey(getSignatureKey())                                                                       //se envia la firma
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //obtener un solo claim
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){                                            //hora 1:20 //se recibe por parametro un Function. el Function a su vez recibe los claims y retorna un T.
        Claims claims = extractAllClaims(token);                                                                        //extrae todos los claims del token.
        return claimsTFunction.apply(claims);
    }

    //obtener el username del token
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);                                                                     //hora 1:26
    }


}
