package tn.sidilec.Exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("message", ex.getReason()); // Le message passé dans ResponseStatusException
        return new ResponseEntity<>(errorBody, ex.getStatusCode());
    }

    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errorBody = new HashMap<>();

        String message = "Une erreur de base de données est survenue.";

        // Optionnel : inspecter le message de l'exception pour détecter le doublon sur 'reference'
        if (ex.getRootCause() != null && ex.getRootCause().getMessage().contains("reference")) {
            message = "Cette référence existe déjà.";
        }

        errorBody.put("message", message);

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}