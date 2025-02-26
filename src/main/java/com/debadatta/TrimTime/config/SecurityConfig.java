package com.debadatta.TrimTime.config;

import java.net.URL;

import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  private URL jwkSetUri;

  @Value("${sample.jwe-key-value}")
  private RSAPrivateKey key;

  @Value("${aws.cognito.logoutUrl}")
  private String logoutUrl;

  @Value("${aws.cognito.logout.success.redirectUrl}")
  private String logoutRedirectUrl;

  @Value("${spring.security.oauth2.client.registration.cognito.clientId}")
  private String clientId;

  private final Environment environment;
  private final JwtFilter jwtFilter;

  @Autowired
  public SecurityConfig(Environment environment, JwtFilter jwtFilter) {
    this.environment = environment;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Disable CSRF
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll() // Allow public access to `/auth/**`
            .requestMatchers("/Barber/**").hasRole("BARBER")
            .anyRequest().authenticated()) // All other requests require authentication
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
        .logout(logout -> logout
            .logoutUrl("/logout") // Define the logout endpoint
            .logoutSuccessHandler(new CustomLogoutHandler(
                environment.getProperty("aws.cognito.logoutUrl"),
                environment.getProperty("aws.cognito.logout.success.logout_url"),
                environment.getProperty("aws.cognito.clientId"))) // Use custom logout handler
            .invalidateHttpSession(true) // Invalidate session
            .deleteCookies("JSESSIONID")) // Optionally delete cookies
        .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class); // Use your JwtFilter

    return http.build();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      try {
        // Check if the authority is of type OidcUserAuthority
        if (!authorities.isEmpty() && authorities.iterator().next() instanceof OidcUserAuthority) {
          OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authorities.iterator().next();
          List<String> roles = (List<String>) oidcUserAuthority.getAttributes().get("cognito:groups");

          mappedAuthorities = roles.stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
              .collect(Collectors.toSet());
        }
      } catch (Exception exception) {
        System.out.println("Error mapping user authorities: " + exception.getMessage());
      }
      return mappedAuthorities;
    };
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5678")); // Replace with actual allowed origins
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT")); // Allowed HTTP methods
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allowed headers
    configuration.setAllowCredentials(true); // Allow credentials (cookies)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
