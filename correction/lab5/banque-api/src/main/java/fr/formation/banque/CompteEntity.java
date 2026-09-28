package fr.formation.banque;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "comptes")
public class CompteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulaire;

    @Enumerated(EnumType.STRING)
    private TypeCompte type;

    private BigDecimal solde;

    private String proprietaire;   // identifiant du client (sub du JWT au lab 7)

    public Long getId() { return id; }
    public String getTitulaire() { return titulaire; }
    public void setTitulaire(String titulaire) { this.titulaire = titulaire; }
    public TypeCompte getType() { return type; }
    public void setType(TypeCompte type) { this.type = type; }
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
    public String getProprietaire() { return proprietaire; }
    public void setProprietaire(String proprietaire) { this.proprietaire = proprietaire; }
}
