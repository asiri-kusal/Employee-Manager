/**
 * The JwtUtils class use generate user tokens.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lk.dialog.ms.security.services.UserDetailsImpl;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    	@Value("${bezkoder.app.jwtSecret}")
    	private String jwtSecret;

    	@Value("${bezkoder.app.jwtExpirationMs}")
    	private int jwtExpirationMs;

    	/**
    	 * Generate token
    	 * @param authentication
    	 * @return token
    	 */
    	public String generateJwtToken(Authentication authentication) {

    		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    		return Jwts.builder()
    				.setSubject((userPrincipal.getUsername()))
    				.setIssuedAt(new Date())
    				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
    				.signWith(SignatureAlgorithm.HS512, jwtSecret)
    				.compact();
    	}

    	/**
    	 * Get user name from token
    	 * @param token
    	 * @return username
    	 */
    	public String getUserNameFromJwtToken(String token) {
    		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    	}

    	/**
    	 * check token validation
    	 * @param authToken
    	 * @return is valid token return true else false
    	 */
    	public boolean validateJwtToken(String authToken) {
    		try {
    			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
    			return true;
    		} catch (SignatureException e) {
    			LOGGER.error("Invalid JWT signature: {}", e.getMessage());
    		} catch (MalformedJwtException e) {
                LOGGER.error("Invalid JWT token: {}", e.getMessage());
    		} catch (ExpiredJwtException e) {
                LOGGER.error("JWT token is expired: {}", e.getMessage());
    		} catch (UnsupportedJwtException e) {
                LOGGER.error("JWT token is unsupported: {}", e.getMessage());
    		} catch (IllegalArgumentException e) {
                LOGGER.error("JWT claims string is empty: {}", e.getMessage());
    		}

    		return false;
    	}

}