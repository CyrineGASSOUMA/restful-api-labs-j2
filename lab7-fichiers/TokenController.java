package fr.formation.banque;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/** Émetteur de tokens pour la formation uniquement. Joue le rôle du serveur d'autorisation. */
@RestController
public class TokenController {

    private final JwtEncoder encoder;

    public TokenController(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/dev/token")
    public Map<String, Object> token(@RequestParam String sub,
                                     @RequestParam(defaultValue = "comptes:lire") String scope) {
        Instant maintenant = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("banque-dev")
                .subject(sub)
                .issuedAt(maintenant)
                .expiresAt(maintenant.plusSeconds(900))
                .claim("scope", scope)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String jeton = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return Map.of("access_token", jeton, "token_type", "Bearer", "expires_in", 900);
    }
}
