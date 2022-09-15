package com.example.demo.security;

import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.demo.model.constant.Constant.*;

@Component
public class JWTAuthenticationVerificationFilter extends BasicAuthenticationFilter {
  public JWTAuthenticationVerificationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  )
      throws IOException, ServletException {
    String token = request.getHeader(AUTHORIZATION);

    if (token == null ||
        !token.startsWith(BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }

    SecurityContextHolder
        .getContext()
        .setAuthentication(
            getAuthentication(request)
        );

    filterChain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(AUTHORIZATION);

    if (token != null) {
      String user = JWT.require(
              HMAC512(SECRET_KEY.getBytes())
          )
          .build()
          .verify(token.replace(BEARER, EMPTY))
          .getSubject();

      if (user != null)
        return new UsernamePasswordAuthenticationToken(
            user,
            null,
            Collections.emptyList()
        );

      return null;
    }
    return null;
  }

}
