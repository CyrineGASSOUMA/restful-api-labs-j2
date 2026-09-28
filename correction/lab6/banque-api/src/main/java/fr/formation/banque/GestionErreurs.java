package fr.formation.banque;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

// ResponseEntityExceptionHandler : les erreurs standard de Spring (JSON illisible, méthode non
// supportée...) sont déjà renvoyées au format Problem Details.
@RestControllerAdvice
public class GestionErreurs extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CompteIntrouvableException.class, VirementIntrouvableException.class})
    ProblemDetail introuvable(RuntimeException e) {
        return probleme(HttpStatus.NOT_FOUND, "Ressource introuvable", e.getMessage(), "introuvable");
    }

    @ExceptionHandler(SoldeInsuffisantException.class)
    ProblemDetail soldeInsuffisant(SoldeInsuffisantException e) {
        return probleme(HttpStatus.UNPROCESSABLE_ENTITY, "Solde insuffisant", e.getMessage(), "solde-insuffisant");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail pd = probleme(HttpStatus.BAD_REQUEST, "Requête invalide",
                "Un ou plusieurs champs sont invalides.", "validation");
        Map<String, String> erreurs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> erreurs.put(f.getField(), f.getDefaultMessage()));
        pd.setProperty("erreurs", erreurs);
        return ResponseEntity.badRequest().body(pd);
    }

    private ProblemDetail probleme(HttpStatus statut, String titre, String detail, String type) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(statut, detail);
        pd.setTitle(titre);
        pd.setType(URI.create("https://api.banque.fr/erreurs/" + type));
        return pd;
    }
}
