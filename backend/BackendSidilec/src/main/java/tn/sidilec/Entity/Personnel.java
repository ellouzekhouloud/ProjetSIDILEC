package tn.sidilec.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import tn.sidilec.Validator.ValidPassword;



@Entity
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "La matricule est obligatoire")
    @Column(unique = true, nullable = false)
    private String matricule;

    private String qualifications;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format de l'email invalide (ex: exemple@domaine.com)")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @ValidPassword // ✅ Annotation personnalisée (voir ci-dessous)
    @Column(nullable = false)
    private String motDePasse;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    private boolean isActive = true;
    
    public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	private String fcmToken; // Ajoute le token FCM

   
    public Personnel() {}

   
    public Personnel(String nom, String prenom, String matricule, String qualifications, String email, String motDePasse, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.qualifications = qualifications;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }
    
   

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
