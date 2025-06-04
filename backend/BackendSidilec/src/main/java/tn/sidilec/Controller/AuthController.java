package tn.sidilec.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;
import tn.sidilec.Repository.PersonnelRepository;
import tn.sidilec.security.JwtService;
import tn.sidilec.service.AuthService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    
    @Autowired
    private PersonnelRepository personnelRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String motDePasse = credentials.get("motDePasse");

        Optional<Personnel> personnel = authService.authenticate(email, motDePasse);

        if (personnel.isPresent()) {
        	
        	// 🔒 Vérification si le compte est désactivé
            if (!personnel.get().isActive()) {
            	Map<String, String> response = new HashMap<>();
            	response.put("message", "Votre compte a été désactivé. Veuillez contacter l'administrateur.");
            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            String accessToken = jwtService.generateAccessToken(email, personnel.get().getNom());
            String refreshToken = jwtService.generateRefreshToken(email); // Générer le refresh token

            Map<String, String> response = new HashMap<>();
            response.put("message", "Connexion réussie");
            response.put("role", personnel.get().getRole().toString());
            response.put("nom", personnel.get().getNom());
            response.put("prenom", personnel.get().getPrenom());
            response.put("accessToken", accessToken);  // Ajouter le JWT dans la réponse
            response.put("refreshToken", refreshToken); // Ajouter le refresh token dans la réponse
            response.put("personnelId", String.valueOf(personnel.get().getId()));

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Échec de l'authentification");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
 // Endpoint pour rafraîchir le token
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");

        try {
            // Extraire l'email depuis le refresh token
            String username = jwtService.extractUsername(refreshToken);
            
            // Vérifier si le refresh token est valide
            if (username != null && !jwtService.isTokenExpired(refreshToken)) {
                // Générer un nouveau token d'accès
                String newAccessToken = jwtService.generateAccessToken(username, ""); // Vous pouvez ajuster selon vos besoins
                Map<String, String> response = new HashMap<>();
                response.put("accessToken", newAccessToken);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Refresh token invalide ou expiré");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Refresh token invalide");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
    
    @PostMapping("/register-admin")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody Personnel personnel) {
    	 
        if (personnelRepository.findByEmail(personnel.getEmail()).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email déjà utilisé.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Définir le rôle en tant qu'ADMIN
        personnel.setRole(Role.ADMIN);

    
        personnel.setMotDePasse(passwordEncoder.encode(personnel.getMotDePasse()));

       
        personnelRepository.save(personnel);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin inscrit avec succès.");
        return ResponseEntity.ok(response);
    }

}
