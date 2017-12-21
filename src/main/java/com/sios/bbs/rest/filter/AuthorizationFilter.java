package com.sios.bbs.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Component
@Order(2)
public class AuthorizationFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if (!httpServletRequest.getRequestURI().equals("/auth")) {

			String authorizationHeader = httpServletRequest.getHeader("Authorization");

			if (authorizationHeader != null) {
				String authorization = authorizationHeader.replaceFirst("Bearer ", "");
				
				try {
					String user = Jwts.parser().setSigningKey("secret".getBytes("UTF-8")).parseClaimsJws(authorization).getBody().getSubject();
					
					Claims claims = Jwts.parser()         
						       .setSigningKey("secret".getBytes("UTF-8"))
						       .parseClaimsJws(authorization).getBody();
										
		            httpServletRequest.setAttribute("id", claims.getId());
		            
		            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		        } catch(SignatureException e) {
		        	e.printStackTrace();
		        	httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        	return;
		        } catch (ExpiredJwtException e) {
		        	e.printStackTrace();
		        	httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        	return;
		        }

			} else {
				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
