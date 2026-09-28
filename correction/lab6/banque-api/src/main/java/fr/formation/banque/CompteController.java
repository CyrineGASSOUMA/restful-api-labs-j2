package fr.formation.banque;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
                                       @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.lister(type, pageable);
    }

    @GetMapping("/{id}")
    public CompteResponse consulter(@PathVariable Long id) {
        return service.consulter(id);
    }

    @PostMapping
    public ResponseEntity<CompteResponse> creer(@Valid @RequestBody CompteRequest requete, UriComponentsBuilder uri) {
        CompteResponse cree = service.creer(requete);
        return ResponseEntity.created(uri.path("/comptes/{id}").buildAndExpand(cree.id()).toUri()).body(cree);
    }

    @PutMapping("/{id}")
    public CompteResponse remplacer(@PathVariable Long id, @Valid @RequestBody CompteRequest requete) {
        return service.remplacer(id, requete);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
