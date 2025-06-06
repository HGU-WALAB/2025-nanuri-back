package com.walab.nanuri.security.config;

import com.walab.nanuri.auth.service.AuthService;
import com.walab.nanuri.commons.filter.ExceptionHandlerFilter;
import com.walab.nanuri.security.filter.JwtTokenFilter;
import com.walab.nanuri.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${image.url}")
    private String imageUrl;

    @Value("${front.base.url1}")
    private String frontBaseUrl1;

    @Value("${front.base.url2}")
    private String frontBaseUrl2;

    @Value("${front.base.url3}")
    private String frontBaseUrl3;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Key key = JwtUtil.getSigningKey(SECRET_KEY);

        http
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource())
                )

                .csrf(AbstractHttpConfigurer::disable)

                .addFilterBefore(new ExceptionHandlerFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenFilter(authService, key), UsernamePasswordAuthenticationFilter.class)

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/api/items/**", "/api/item/**", "/api/want/**").permitAll()
                    .antMatchers(imageUrl + "**").permitAll()
                    .antMatchers("/api/nanuri/auth/**", "/error", "/file/**").permitAll()
                    .antMatchers("/api/nanuri/**", "/api/**", "/ws-stomp/**").authenticated();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000", frontBaseUrl1, frontBaseUrl2, frontBaseUrl3));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PATCH", "DELETE", "PUT"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}