package com.project.quickcars2.util;

import com.project.quickcars2.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        String authHeader = httpReq.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.isTokenValid(token)) {
                    String username = jwtUtil.extractUsername(token);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            new User(username, "", Collections.emptyList()), null, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            chain.doFilter(request, response);
        } catch (JwtException e) {
            HttpServletResponse httpRes = (HttpServletResponse) response;
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return;
        }



    }
}
