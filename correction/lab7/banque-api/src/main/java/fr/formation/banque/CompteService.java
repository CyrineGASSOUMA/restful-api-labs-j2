package fr.formation.banque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class CompteService {

    private final CompteRepository repository;

    public CompteService(CompteRepository repository) {
        this.repository = repository;
    }

    public Page<CompteResponse> lister(String utilisateur, TypeCompte type, Pageable pageable) {
        Page<CompteEntity> page = (type == null)
                ? repository.findByProprietaire(utilisateur, pageable)
                : repository.findByProprietaireAndType(utilisateur, type, pageable);
        return page.map(CompteMapper::toResponse);
    }

    public CompteResponse consulter(Long id, String utilisateur) {
        return CompteMapper.toResponse(chargerPourProprietaire(id, utilisateur));
    }

    @Transactional
    public CompteResponse creer(CompteRequest requete, String utilisateur) {
        CompteEntity entity = CompteMapper.toEntity(requete);
        entity.setProprietaire(utilisateur);
        entity.setSolde(BigDecimal.ZERO);          // le solde n'évolue que par des virements
        return CompteMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CompteResponse remplacer(Long id, CompteRequest requete, String utilisateur) {
        CompteEntity entity = chargerPourProprietaire(id, utilisateur);
        entity.setTitulaire(requete.titulaire());
        entity.setType(requete.type());
        return CompteMapper.toResponse(entity);    // sauvegarde automatique en fin de transaction
    }

    @Transactional
    public void supprimer(Long id, String utilisateur) {
        repository.delete(chargerPourProprietaire(id, utilisateur));
    }

    // Protection BOLA : un client ne voit que ses comptes. 404 plutôt que 403 pour ne rien révéler.
    private CompteEntity chargerPourProprietaire(Long id, String utilisateur) {
        CompteEntity entity = repository.findById(id).orElseThrow(() -> new CompteIntrouvableException(id));
        if (!entity.getProprietaire().equals(utilisateur)) {
            throw new CompteIntrouvableException(id);
        }
        return entity;
    }
}
