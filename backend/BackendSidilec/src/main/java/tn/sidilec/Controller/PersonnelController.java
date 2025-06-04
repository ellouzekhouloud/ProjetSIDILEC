package tn.sidilec.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;
import tn.sidilec.Repository.PersonnelRepository;
import tn.sidilec.service.PersonnelService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  
@RequestMapping("/api/personnels")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private PersonnelRepository personnelRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Personnel> getAllPersonnels() {
        return personnelService.getAllPersonnels();
    }

    @GetMapping("/{id}")
    public Optional<Personnel> getPersonnelById(@PathVariable Long id) {
        return personnelService.getPersonnelById(id);
    }
    @PostMapping
    public ResponseEntity<?> addPersonnel(@Valid @RequestBody Personnel personnel, BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        // ⚠️ Erreurs de validation (notations @Valid)
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        // ✔ Vérifier l’unicité de la matricule
        if (personnelRepository.findByMatricule(personnel.getMatricule()).isPresent()) {
            errors.put("matricule", "Matricule déjà utilisé !");
        }

        // ✔ Vérifier l’unicité de l’email
        if (personnelRepository.findByEmail(personnel.getEmail()).isPresent()) {
            errors.put("email", "Email déjà utilisé !");
        }

        // ✔ Vérifier le rôle
        if (personnel.getRole() == null) {
            errors.put("role", "Le rôle doit être spécifié (RESPONSABLE_RECEPTION ou CONTROLEUR)");
        } else if (!personnel.getRole().equals(Role.RESPONSABLE_RECEPTION) && !personnel.getRole().equals(Role.CONTROLEUR)) {
            errors.put("role", "Rôle invalide. Le rôle doit être soit 'RESPONSABLE_RECEPTION' soit 'CONTROLEUR'");
        }

        // ⛔ S'il y a des erreurs, on retourne 400
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // 🔐 Encoder le mot de passe
        personnel.setMotDePasse(passwordEncoder.encode(personnel.getMotDePasse()));

        // ✅ Sauvegarde
        return ResponseEntity.ok(personnelRepository.save(personnel));
    }

    @PutMapping("/{id}")
    public Personnel updatePersonnel(@PathVariable Long id, @RequestBody Personnel personnel) {
        return personnelService.updatePersonnel(id, personnel);
    }

    @DeleteMapping("/{id}")
    public void deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
    }
    
    @GetMapping("/personne/{nom}")
    public ResponseEntity<Personnel> getPersonnelByNom(@PathVariable String nom) {
        Personnel personnel = personnelRepository.findByNom(nom)
                    .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));
        return ResponseEntity.ok(personnel);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivatePersonnel(@PathVariable Long id) {
        Optional<Personnel> personnelOpt = personnelRepository.findById(id);

        if (personnelOpt.isPresent()) {
            Personnel personnel = personnelOpt.get();
            personnel.setActive(false);
            personnelRepository.save(personnel);
            return ResponseEntity.ok("Personnel désactivé avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personnel non trouvé !");
        }
    }
    @GetMapping("/actifs")
    public List<Personnel> getActivePersonnels() {
        return personnelService.getActivePersonnels();
    }
}