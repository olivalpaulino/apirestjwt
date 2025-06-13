package dobackaofront.apirestjwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

    private final Key signingKey;
    private final long expirationMs = 3600000; // 1 hora (em milissegundos)

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // Gera a chave de assinatura a partir do secret (HS256)
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Gera um token JWT para um determinado usuário (username e role)
    public String gerarToken(String username, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)                   // assunto do token: identificação do usuário
                .claim("role", role)                    // adiciona informação de role como claim customizado
                .setIssuedAt(agora)                     // data de criação do token
                .setExpiration(expiracao)               // data de expiração
                .signWith(signingKey, SignatureAlgorithm.HS256) // assina com HS256 e nossa chave secreta
                .compact();
    }

    // Valida e parseia um token JWT (retorna o JWS claims se válido)
    public Jws<Claims> validarToken(String token) throws JwtException {
        // Lança JwtException (subclasses: ExpiredJwtException, SignatureException, etc.) se inválido
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }

    // Extrai username do token (assumindo token válido)
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extrai role do token
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
