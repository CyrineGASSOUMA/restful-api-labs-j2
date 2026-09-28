package fr.formation.banque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<CompteEntity, Long> {

    Page<CompteEntity> findByProprietaire(String proprietaire, Pageable pageable);

    Page<CompteEntity> findByProprietaireAndType(String proprietaire, TypeCompte type, Pageable pageable);
}
