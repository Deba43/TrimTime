package com.debadatta.TrimTime.config;

import java.net.URL;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
  private final JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
  private final EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;

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

  private final JwtFilter jwtFilter;

  public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(requests -> requests
                      .requestMatchers("/auth/**").permitAll()
                      .anyRequest().authenticated())
              .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return new NimbusJwtDecoder(jwtProcessor());
  }

  private JWTProcessor<SecurityContext> jwtProcessor() {
    JWKSource<SecurityContext> jwsJwkSource = new RemoteJWKSet<>(this.jwkSetUri);
    JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(this.jwsAlgorithm, jwsJwkSource);

    JWKSource<SecurityContext> jweJwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey()));
    JWEKeySelector<SecurityContext> jweKeySelector = new JWEDecryptionKeySelector<>(this.jweAlgorithm,
        this.encryptionMethod, jweJwkSource);

    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
    jwtProcessor.setJWSKeySelector(jwsKeySelector);
    jwtProcessor.setJWEKeySelector(jweKeySelector);

    return jwtProcessor;
  }

  private RSAKey rsaKey() {
    RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) this.key;
    return new RSAKey.Builder(Base64URL.encode(crtKey.getModulus()), Base64URL.encode(crtKey.getPublicExponent()))
        .privateKey(this.key)
        .keyUse(KeyUse.ENCRYPTION)
        .build();
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      try {
        OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) new ArrayList<>(authorities).get(0);
        mappedAuthorities = ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups"))
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
      } catch (Exception exception) {
        System.out.println("Not Authorized: " + exception.getMessage());
      }
      return mappedAuthorities;
    };
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5678"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
