package tn.sidilec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("65f4337d-40f8-4747-a409-71dee7805d2b".getBytes());

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Générer le token d'accès (JWT)
    public String generateAccessToken(String username, String nom) {
        return Jwts.builder()
            .setSubject(username)
            .claim("nom", nom)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expiration de 24h
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Générer un refresh token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30)) // Expiration de 30 jours pour le refresh token
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Extraire le nom de l'utilisateur du token
    public String extractNom(String token) {
        return extractClaim(token, claims -> claims.get("nom", String.class));
    }

    // Extraire le nom d'utilisateur (email)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire une réclamation (claim) spécifique du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Vérifier si le token est expiré
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Valider un token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
