package fr.formation.banque;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

// Lab 5 : le controller renvoie encore l'entité. Corrigé au lab 6 avec des DTO.
@RestController
@RequestMapping("/comptes")
public class CompteController {

    private final CompteService service;

    public CompteController(CompteService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompteEntity> lister(@RequestParam(required = false) TypeCompte type) {
        return service.lister(type);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompteEntity> consulter(@PathVariable Long id) {
        return ResponseEntity.of(service.consulter(id));
    }

    @PostMapping
    public ResponseEntity<CompteEntity> creer(@RequestBody CompteEntity compte, UriComponentsBuilder uri) {
        CompteEntity cree = service.creer(compte);
        return ResponseEntity.created(uri.path("/comptes/{id}").buildAndExpand(cree.getId()).toUri()).body(cree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompteEntity> remplacer(@PathVariable Long id, @RequestBody CompteEntity compte) {
        return ResponseEntity.of(service.remplacer(id, compte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        return service.supprimer(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
