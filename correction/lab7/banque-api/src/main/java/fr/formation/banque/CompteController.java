package fr.formation.banque;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/comptes")
public class CompteController {

    private final CompteService service;

    public CompteController(CompteService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CompteResponse> lister(@RequestParam(required = false) TypeCompte type,
                                       @PageableDefault(size = 10, sort = "id") Pageable pageable,
                                       @AuthenticationPrincipal Jwt jwt) {
        return service.lister(jwt.getSubject(), type, pageable);
    }

    @GetMapping("/{id}")
    public CompteResponse consulter(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return service.consulter(id, jwt.getSubject());
    }

    @PostMapping
    public ResponseEntity<CompteResponse> creer(@Valid @RequestBody CompteRequest requete,
                                                @AuthenticationPrincipal Jwt jwt,
                                                UriComponentsBuilder uri) {
        CompteResponse cree = service.creer(requete, jwt.getSubject());
        return ResponseEntity.created(uri.path("/comptes/{id}").buildAndExpand(cree.id()).toUri()).body(cree);
    }

    @PutMapping("/{id}")
    public CompteResponse remplacer(@PathVariable Long id, @Valid @RequestBody CompteRequest requete,
                                    @AuthenticationPrincipal Jwt jwt) {
        return service.remplacer(id, requete, jwt.getSubject());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        service.supprimer(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
