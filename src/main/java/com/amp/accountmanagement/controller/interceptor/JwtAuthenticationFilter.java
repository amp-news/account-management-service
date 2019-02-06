package com.amp.accountmanagement.controller.interceptor;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.amp.accountmanagement.model.entity.AccountStatus;
import com.amp.accountmanagement.model.entity.AuthDetails;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.amp.accountmanagement.model.entity.Claim.USER_EMAIL;
import static com.amp.accountmanagement.model.entity.Claim.USER_ROLE;
import static com.amp.accountmanagement.model.entity.Claim.USER_STATUS;
import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String ACCESS_TOKEN_TYPE = "Bearer";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String jwt = retrieveToken(request);
    if (Objects.nonNull(jwt)) {
      final Authentication authentication = getAuthentication(request, JWT.decode(jwt));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  public @Nullable String retrieveToken(HttpServletRequest request) {
    String token = null;

    String bearerToken = request.getHeader("Authorization");
    if (hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_TYPE)) {
      token = bearerToken.replace(ACCESS_TOKEN_TYPE, "").trim();
    }

    return token;
  }

  private Authentication getAuthentication(HttpServletRequest request, DecodedJWT jwt) {
    final AuthDetails authDetails = new AuthDetails();
    authDetails.setId(Long.valueOf(jwt.getSubject()));
    authDetails.setUsername(jwt.getClaim(USER_EMAIL.getName()).asString());
    authDetails.setPassword(jwt.getToken());
    authDetails.setStatus(AccountStatus.fromName(jwt.getClaim(USER_STATUS.getName()).asString()));
    authDetails.setRole(jwt.getClaim(USER_ROLE.getName()).asString());

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            authDetails, authDetails.getPassword(), authDetails.getAuthorities());
    authentication.setDetails(getAuthenticationDetails(request));

    return authentication;
  }

  private WebAuthenticationDetails getAuthenticationDetails(HttpServletRequest request) {
    return new WebAuthenticationDetailsSource().buildDetails(request);
  }
}
