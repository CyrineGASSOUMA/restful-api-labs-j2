package fr.formation.banque;

import fr.formation.banque.api.model.Virement;
import fr.formation.banque.api.model.VirementRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VirementService {

    private final CompteRepository comptes;
    // Virements en mémoire pour rester simple (une table dédiée en vrai)
    private final Map<Long, Virement> virements = new ConcurrentHashMap<>();
    private final Map<String, Virement> parCleIdempotence = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public VirementService(CompteRepository comptes) {
        this.comptes = comptes;
    }

    @Transactional
    public Virement creer(VirementRequest requete, String cle, String utilisateur) {
        if (cle != null && parCleIdempotence.containsKey(cle)) {
            return parCleIdempotence.get(cle);                        // doublon : même réponse, aucun débit
        }
        CompteEntity source = comptes.findById(requete.getCompteSource())
                .filter(c -> c.getProprietaire().equals(utilisateur))  // BOLA : on ne vire que depuis ses comptes
                .orElseThrow(() -> new CompteIntrouvableException(requete.getCompteSource()));
        CompteEntity destination = comptes.findById(requete.getCompteDestination())
                .orElseThrow(() -> new CompteIntrouvableException(requete.getCompteDestination()));

        if (source.getSolde().compareTo(requete.getMontant()) < 0) {
            throw new SoldeInsuffisantException(source.getId(), source.getSolde(), requete.getMontant());
        }
        source.setSolde(source.getSolde().subtract(requete.getMontant()));
        destination.setSolde(destination.getSolde().add(requete.getMontant()));

        Virement virement = new Virement()
                .id(sequence.incrementAndGet())
                .compteSource(source.getId())
                .compteDestination(destination.getId())
                .montant(requete.getMontant())
                .libelle(requete.getLibelle())
                .statut("EXECUTE");
        virements.put(virement.getId(), virement);
        if (cle != null) {
            parCleIdempotence.put(cle, virement);
        }
        return virement;
    }

    public Virement lire(Long id) {
        Virement virement = virements.get(id);
        if (virement == null) {
            throw new VirementIntrouvableException(id);
        }
        return virement;
    }
}
