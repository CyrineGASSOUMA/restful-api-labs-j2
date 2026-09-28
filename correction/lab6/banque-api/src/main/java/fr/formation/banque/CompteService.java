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

    public Page<CompteResponse> lister(TypeCompte type, Pageable pageable) {
        Page<CompteEntity> page = (type == null) ? repository.findAll(pageable) : repository.findByType(type, pageable);
        return page.map(CompteMapper::toResponse);
    }

    public CompteResponse consulter(Long id) {
        return CompteMapper.toResponse(charger(id));
    }

    @Transactional
    public CompteResponse creer(CompteRequest requete) {
        CompteEntity entity = CompteMapper.toEntity(requete);
        entity.setSolde(BigDecimal.ZERO);          // le solde n'évolue que par des virements
        return CompteMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public CompteResponse remplacer(Long id, CompteRequest requete) {
        CompteEntity entity = charger(id);
        entity.setTitulaire(requete.titulaire());
        entity.setType(requete.type());
        return CompteMapper.toResponse(entity);
    }

    @Transactional
    public void supprimer(Long id) {
        repository.delete(charger(id));
    }

    private CompteEntity charger(Long id) {
        return repository.findById(id).orElseThrow(() -> new CompteIntrouvableException(id));
    }
}
