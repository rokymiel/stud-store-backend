package ru.hse.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class JwtUtils {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public static String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        return token != null && token.startsWith(TOKEN_PREFIX) ? token.substring(TOKEN_PREFIX.length()) : null;
    }

    public static Boolean isTokenExpired(Claims claims) {
        return claims == null || claims.getExpiration().before(new Date());
    }

    public static Claims resolveClaims(HttpServletRequest request, String secretKey) {
        try {
            return parseJwtClaims(resolveToken(request), secretKey);
        } catch (ExpiredJwtException e) {
            request.setAttribute("JWT is expired", e.getMessage());
            return null;
        } catch (Exception e) {
            request.setAttribute("JWT is invalid", e.getMessage());
            return null;
        }
    }

    private static Claims parseJwtClaims(String token, String secretKey) {
        return token == null ? null : Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
