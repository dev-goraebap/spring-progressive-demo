package xyz.goraebap.spring_progressive_demo.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import xyz.goraebap.spring_progressive_demo.shared.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthFilter jwtAuthFilter;

    @Value("${swagger.username}")
    private String swaggerUsername;

    @Value("${swagger.password}")
    private String swaggerPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // CSRF disabled for API (enable if using session-based auth)
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless session (for REST API)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/builds/**", "/.vite/**", "/favicon.ico", "/bgm/**").permitAll()

                        // Public endpoints - Pages
                        .requestMatchers("/", "/posts/**", "/series/**", "/curations/**", "/patch-notes/**", "/login", "/error").permitAll()

                        // Public endpoints - Health check
                        .requestMatchers("/actuator/health").permitAll()

                        // Public endpoints - All API endpoints (change this in production!)
                        .requestMatchers("/api/**").permitAll()

                        // Admin login page - permitAll
                        .requestMatchers("/admin/login").permitAll()

                        // Admin endpoints - Require ADMIN role
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Swagger/API docs - Require authentication
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/api-docs.html").authenticated()

                        // All other requests permit (change in production!)
                        .anyRequest().permitAll()
                )
                // Admin 페이지 미인증 시 로그인 페이지로 리다이렉트
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String requestUri = request.getRequestURI();
                            if (requestUri.startsWith("/admin")) {
                                response.sendRedirect("/admin/login");
                            } else {
                                response.sendError(401);
                            }
                        })
                )
                // Enable HTTP Basic Authentication (for Swagger)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(swaggerUsername)
                .password(passwordEncoder().encode(swaggerPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
