package fr.formation.banque;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompteService {

    private final CompteRepository repository;

    public CompteService(CompteRepository repository) {
        this.repository = repository;
    }

    public List<CompteEntity> lister(TypeCompte type) {
        return type == null ? repository.findAll() : repository.findByType(type);
    }

    public Optional<CompteEntity> consulter(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public CompteEntity creer(CompteEntity compte) {
        return repository.save(compte);
    }

    @Transactional
    public Optional<CompteEntity> remplacer(Long id, CompteEntity nouveau) {
        return repository.findById(id).map(compte -> {
            compte.setTitulaire(nouveau.getTitulaire());
            compte.setType(nouveau.getType());
            compte.setSolde(nouveau.getSolde());
            return compte;                       // écrit en base au commit (dirty checking)
        });
    }

    @Transactional
    public boolean supprimer(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
