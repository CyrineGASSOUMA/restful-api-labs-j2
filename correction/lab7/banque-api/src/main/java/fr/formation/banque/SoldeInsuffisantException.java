package fr.formation.banque;

import java.math.BigDecimal;

public class SoldeInsuffisantException extends RuntimeException {
    public SoldeInsuffisantException(Long compte, BigDecimal solde, BigDecimal montant) {
        super("Le compte " + compte + " a un solde de " + solde + " €, montant demandé " + montant + " €.");
    }
}
