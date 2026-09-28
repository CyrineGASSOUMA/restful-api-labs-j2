package fr.formation.banque;

import java.math.BigDecimal;

public record CompteResponse(Long id, String titulaire, TypeCompte type, BigDecimal solde) { }
