package com.increff.pos.spring;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getServletPath().equals("/login")) {
                filterChain.doFilter(request, response);
            } else {
                Cookie[] co = request.getCookies();
                if (co == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Optional<String> access_token = stream(request.getCookies())
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findAny();


                if (access_token.isPresent()) {
                    try {
                        String token = access_token.get();
                        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = jwtVerifier.verify(token);
                        String username = decodedJWT.getSubject();
                        String role = decodedJWT.getClaim("roles").asString();
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                        authorities.add(new SimpleGrantedAuthority(role));

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } catch (NestedServletException e) {
                        throw e;
                    } catch (Exception exception) {
                        response.setHeader("error", exception.getMessage());
                        response.setStatus(FORBIDDEN.value());

                        Map<String, String> tokens = new HashMap<>();
                        tokens.put("error_message", exception.getMessage());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

                    }

                } else {
                    filterChain.doFilter(request, response);
                }

            }
        } catch (NestedServletException e) {
            response.sendRedirect("/404");
        } catch (Exception e) {
            new ObjectMapper().writeValue(response.getOutputStream(), e);
        }
    }
}
