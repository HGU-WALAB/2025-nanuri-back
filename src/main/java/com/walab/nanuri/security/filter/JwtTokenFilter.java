package com.walab.nanuri.security.filter;

import com.walab.nanuri.auth.service.AuthService;
import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final Key SECRET_KEY;

    private boolean isExcludedPath(HttpServletRequest request) {
        String uri = request.getServletPath();
        String method = request.getMethod();

        return (
                ("GET".equals(method) && uri.matches("/api/items(/.*)?$")) ||
                        ("GET".equals(method) && uri.matches("/api/item(/.*)?$")) ||
                        ("GET".equals(method) && uri.matches("/api/want(/.*)?$")) ||

                        "/api/nanuri/auth/login".equals(uri) ||
                        "/api/nanuri/auth/signup".equals(uri) ||
                        "/api/nanuri/auth/logout".equals(uri) ||
                        uri.matches("/assets(/.*)?$")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        Cookie[] cookies = request.getCookies();

        if (isExcludedPath(request) && cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken_handful = null;
        String refreshToken_handful = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken_handful".equals(cookie.getName())) {
                    accessToken_handful = cookie.getValue();
                }
                if ("refreshToken_handful".equals(cookie.getName())) {
                    refreshToken_handful = cookie.getValue();
                }
            }
        }

        try {
            String userId = JwtUtil.getUserId(accessToken_handful, SECRET_KEY);
            User loginUser = authService.getLoginUser(userId);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser.getUniqueId(), null, null);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (RuntimeException e) {
            log.info("❗ {}", e.getMessage());

            // accessToken이 만료된 경우, refreshToken으로 재발급 시도
            // JwtTokenFilter.java에서 리프레시 토큰 처리 부분 수정:
            if (refreshToken_handful != null) {
                try {
                    String userId = JwtUtil.getUserId(refreshToken_handful, SECRET_KEY);
                    User loginUser = authService.getLoginUser(userId);

                    // 새로운 만료 시간으로 액세스 토큰 생성
                    String newAccessToken = authService.createAccessToken(
                            loginUser.getUniqueId(),
                            loginUser.getName(),
                            loginUser.getEmail()
                    );

                    // 새 액세스 토큰을 쿠키로 설정
                    Cookie newAccessTokenCookie = new Cookie("accessToken_handful", newAccessToken);
                    newAccessTokenCookie.setHttpOnly(true);
                    newAccessTokenCookie.setPath("/");
                    newAccessTokenCookie.setMaxAge(7200); // 토큰 만료 시간과 일치 (2시간)
                    response.addCookie(newAccessTokenCookie);

                    // 인증 설정
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(loginUser.getUniqueId(), null, null);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception refreshEx) {
                    // 더 상세한 로깅을 포함한 개선된 예외 처리
                    log.error("❌ 토큰 리프레시 실패: {}", refreshEx.getMessage());
                    respondUnauthorized(response);
                    return;
                }
            } else {
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void respondUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\": \"" + "refreshToken이 존재하지 않습니다. 로그인이 필요합니다." + "\"}");
    }
}