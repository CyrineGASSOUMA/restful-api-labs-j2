package fr.formation.banque;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<CompteEntity, Long> {

    Page<CompteEntity> findByType(TypeCompte type, Pageable pageable);
}
