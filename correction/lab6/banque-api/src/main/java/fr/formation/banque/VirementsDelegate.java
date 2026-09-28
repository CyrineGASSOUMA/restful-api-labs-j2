package fr.formation.banque;

import fr.formation.banque.api.VirementsApiDelegate;
import fr.formation.banque.api.model.Virement;
import fr.formation.banque.api.model.VirementRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Seule classe écrite à la main pour les virements : la logique derrière le contrat.
// Le controller, l'interface et les modèles sont générés depuis openapi.yaml.
@Service
public class VirementsDelegate implements VirementsApiDelegate {

    private final VirementService service;

    public VirementsDelegate(VirementService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Virement> creerVirement(VirementRequest virementRequest, String idempotencyKey) {
        Virement virement = service.creer(virementRequest, idempotencyKey);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/virements/{id}").buildAndExpand(virement.getId()).toUri();
        return ResponseEntity.created(location).body(virement);
    }

    @Override
    public ResponseEntity<Virement> lireVirement(Long id) {
        return ResponseEntity.ok(service.lire(id));
    }
}
