package fr.formation.banque;

import fr.formation.banque.api.VirementsApiDelegate;
import fr.formation.banque.api.model.Virement;
import fr.formation.banque.api.model.VirementRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Seule classe écrite à la main pour les virements : la logique derrière le contrat.
// Le controller, l'interface et les modèles sont générés depuis openapi.yaml.
@Service
public class VirementsDelegate implements VirementsApiDelegate {

    private final Map<Long, Virement> virements = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public ResponseEntity<Virement> creerVirement(VirementRequest requete) {
        Virement virement = new Virement()
                .id(sequence.incrementAndGet())
                .compteSource(requete.getCompteSource())
                .compteDestination(requete.getCompteDestination())
                .montant(requete.getMontant())
                .libelle(requete.getLibelle())
                .statut("EXECUTE");
        virements.put(virement.getId(), virement);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/virements/{id}").buildAndExpand(virement.getId()).toUri();
        return ResponseEntity.created(location).body(virement);
    }

    @Override
    public ResponseEntity<Virement> lireVirement(Long id) {
        Virement virement = virements.get(id);
        return virement == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(virement);
    }
}
