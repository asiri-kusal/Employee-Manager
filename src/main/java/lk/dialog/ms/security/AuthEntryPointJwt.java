/**
 * The AuthEntryPointJwt class use to check authorization.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint{

        private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);

        	/**
        	 * Check Authorization for users
        	 * @param request
        	 * @param response
        	 * @param authException
        	 * @throws IOException
        	 * @throws ServletException
        	 */
        	@Override
        	public void commence(HttpServletRequest request, HttpServletResponse response,
        			AuthenticationException authException) throws IOException, ServletException {
        		LOGGER.error("Unauthorized error: {}", authException.getMessage());
        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        	}

}