package fr.formation.banque;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompteRepository extends JpaRepository<CompteEntity, Long> {

    List<CompteEntity> findByType(TypeCompte type);
}
