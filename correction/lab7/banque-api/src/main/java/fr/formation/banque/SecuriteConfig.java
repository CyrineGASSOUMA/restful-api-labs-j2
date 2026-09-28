package fr.formation.banque;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecuriteConfig {

    // Clé de signature de DÉVELOPPEMENT. En production, les clés publiques viennent du serveur
    // d'autorisation (Keycloak, Entra ID...) via spring.security.oauth2.resourceserver.jwt.issuer-uri
    @Value("${securite.jwt.secret}")
    private String secret;

    private SecretKey cle() {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(cle()).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("banque-dev"));
        return decoder;
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(cle()));
    }

    @Bean
    SecurityFilterChain securite(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/dev/token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comptes/**", "/virements/**").hasAuthority("SCOPE_comptes:lire")
                        .requestMatchers("/comptes/**").hasAuthority("SCOPE_comptes:ecrire")
                        .requestMatchers(HttpMethod.POST, "/virements").hasAuthority("SCOPE_virements:creer")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
                .build();
    }
}
