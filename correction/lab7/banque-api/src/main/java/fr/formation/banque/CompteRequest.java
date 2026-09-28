package fr.formation.banque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CompteRequest(
        @NotBlank @Size(max = 100) String titulaire,
        @NotNull TypeCompte type) { }
