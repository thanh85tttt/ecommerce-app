package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.demo.model.constant.Constant.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest req,
      HttpServletResponse res
  ) throws AuthenticationException {
    try {
      User credentials = new ObjectMapper()
          .readValue(
              req.getInputStream(),
              User.class
          );

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              credentials.getUsername(),
              credentials.getPassword(),
              Collections.emptyList()
          )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth
  ) throws IOException, ServletException {
    String subject = (
        (org.springframework.security.core.userdetails.User) auth.getPrincipal()
    ).getUsername();
    long iat = System.currentTimeMillis();
    Date issueAt = new Date(iat);
    Date expiresAt = new Date(iat + EXPIRATION_TIME);
    String token = JWT.create()
        .withSubject(subject)
        .withIssuedAt(issueAt)
        .withExpiresAt(expiresAt)
        .sign(HMAC512(SECRET_KEY.getBytes()));
    res.addHeader(AUTHORIZATION, BEARER + token);
  }
}
