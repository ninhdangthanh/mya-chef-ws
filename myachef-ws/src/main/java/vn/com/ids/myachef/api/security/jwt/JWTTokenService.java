package vn.com.ids.myachef.api.security.jwt;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import vn.com.ids.myachef.api.security.userdetails.UserDetailsImpl;
import vn.com.ids.myachef.business.config.ApplicationConfig;

@Component
@Slf4j
public class JWTTokenService {

    private static final String USERNAME = "username";
    private static final String TYPE = "type";

    @Autowired
    private ApplicationConfig applicationConfig;

    public String generateJwtToken(UserDetailsImpl userDetails, JwtTokenType jwtTokenType) {
        String userId = userDetails.getId().toString();
        String username = userDetails.getUsername();
        String type = null;
        if (userDetails.getAuthorities().toString().contains(UserDetailsImpl.CUSTOMER_ROLE)) {
            type = UserDetailsImpl.CUSTOMER_ROLE;
        }

        if (jwtTokenType == JwtTokenType.TOKEN) {
            return generateJwtToken(userId, username, type, applicationConfig.getJwtExpirationMs());
        } else {
            return generateJwtToken(userId, username, type, applicationConfig.getJwtRefreshTokenExpirationMs());
        }
    }

    public String generateJwtToken(String userId, String username, String type, Long expirationMs) {
        Date issueAtTime = new Date();
        return Jwts.builder() //
                .setSubject(userId) //
                .claim(USERNAME, username) //
                .claim(TYPE, type)//
                .setIssuedAt(issueAtTime) //
                .setId(UUID.randomUUID().toString()) //
                .setExpiration(new Date(issueAtTime.getTime() + expirationMs)) //
                .signWith(SignatureAlgorithm.HS512, applicationConfig.getJwtSecret()) //
                .compact();
    }

    public Long getUserId(String token) {
        Long userId = 0L;
        try {
            Claims claims = getAllClaimsFromToken(token);
            userId = Long.parseLong(claims.getSubject());
        } catch (NumberFormatException ex) {
            log.error(ex.getMessage(), ex);
        }
        return userId;
    }

    public String getType(String token) {
        return getKeyFromToken(token, TYPE);
    }

    public Object getObjectFromClaims(String token, String key) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(key);
    }

    public String getKeyFromToken(String token, String key) {
        return getObjectFromClaims(token, key) != null ? getObjectFromClaims(token, key).toString() : null;
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDate(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAt(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(applicationConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String authToken) {
        boolean isValidToken = false;
        try {
            Jwts.parser().setSigningKey(applicationConfig.getJwtSecret()).parseClaimsJws(authToken);
            isValidToken = true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return isValidToken;
    }

    public String parseJwt(HttpServletRequest request) {
        String token = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            token = headerAuth.substring(7, headerAuth.length());
        }

        return token;
    }

    public enum JwtTokenType {
        TOKEN, REFRESH
    }

}
