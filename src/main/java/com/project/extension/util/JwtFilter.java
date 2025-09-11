package com.project.extension.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider jwtUtil;

    public JwtFilter() {
        log.info("JwtAuthenticationFilter instanciado");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JwtAuthenticationFilter.doFilterInternal chamado");

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extrairUsername(token);

            log.info("Username extraído do token: " + email);

            if (email != null) {
                if (jwtUtil.validarToken(token, email)) {
                    List<SimpleGrantedAuthority> authorities = extractAuthoritiesFromToken(token);

                    log.info("Authorities extraídas: " + authorities);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Token JWT inválido para o usuário: " + email);
                }
            } else {
                log.warn("Email extraído do token é nulo");
            }
        } else {
            log.warn("Cabeçalho Authorization ausente ou inválido");
        }

        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromToken(String token) {
        try {
            Claims claims = jwtUtil.getClaims(token);
            Object rawRoles = claims.get("roles");

            if (rawRoles instanceof List<?>) {
                return ((List<?>) rawRoles).stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            } else {
                log.warn("'roles' claim não é uma lista. Valor bruto: " + rawRoles);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Erro ao extrair roles do token", e);
            return Collections.emptyList();
        }
    }
}