package fr.formation.banque;

public class CompteIntrouvableException extends RuntimeException {
    public CompteIntrouvableException(Long id) {
        super("Le compte " + id + " n'existe pas.");
    }
}
