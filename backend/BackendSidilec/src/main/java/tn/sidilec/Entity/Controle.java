package tn.sidilec.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




@Entity
@Getter
@Setter
@ToString
public class Controle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String reference;
    private String fournisseur;
    private String verificateur;
    private String numBL;
    private LocalDate dateDeControle;
    private String raisonRefus;
    
    private Integer quantite; // Quantité mesurée
    private String quantiteStatus; // Status de la quantité : "valid" ou "invalid"
    private Integer quantiteIncorrecte;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    
    private Produit produit;

    @OneToMany(mappedBy = "controle", cascade = CascadeType.ALL)
   
    @JsonIgnoreProperties("controle") // Ignore la propriété "controle" dans ResultatControle lors de la sérialisation
    private List<ResultatControle> resultatsControle;
   
   
    
   
}