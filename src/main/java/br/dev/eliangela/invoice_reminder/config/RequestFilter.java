package br.dev.eliangela.invoice_reminder.config;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.dev.eliangela.invoice_reminder.core.model.schema.security.UserSchema;
import br.dev.eliangela.invoice_reminder.core.service.security.JwtService;
import br.dev.eliangela.invoice_reminder.util.DateTimeUtil;
import br.dev.eliangela.invoice_reminder.web.error.handler.RequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PropertiesFileConfiguration propertiesFileConfiguration;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String userSubject;

        if (isPathInWhitelist(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        final String token = authorizationHeader == null ? "" : authorizationHeader.replace("Bearer ", "");

        if (StringUtils.isEmpty(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não informado.");
            return;
        }

        userSubject = jwtService.extractSubject(token);
        Date expiration = jwtService.extractExpiration(token);

        if (StringUtils.isNotEmpty(userSubject)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (!jwtService.isTokenValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado.");
                return;
            }

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            UserDetails userDetails = UserSchema.builder()
                    .email(userSubject)
                    .expirationDate(DateTimeUtil.getLocalDateFromDate(expiration))
                    .build();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);

            SecurityContextHolder.setContext(context);
        }

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest wrappedRequest = new RequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
    }

    private boolean isPathInWhitelist(HttpServletRequest request) {
        List<String> whiteList = propertiesFileConfiguration.getSecurityWhiteList();

        for (String permittedUri : whiteList) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(permittedUri);
            if (matcher.matches(request)) {
                return true;
            }
        }

        return false;
    }
}
