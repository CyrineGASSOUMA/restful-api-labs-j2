package fr.formation.banque;

public class VirementIntrouvableException extends RuntimeException {
    public VirementIntrouvableException(Long id) {
        super("Le virement " + id + " n'existe pas.");
    }
}
