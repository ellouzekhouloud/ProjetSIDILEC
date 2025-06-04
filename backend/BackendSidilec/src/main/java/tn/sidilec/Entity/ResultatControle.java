package tn.sidilec.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ResultatControle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String valeurMesuree; // Null si contrôle visuel
    private String visuel; // Null si c'est une mesure
    
   
    

    @ManyToOne
    @JoinColumn(name = "plan_controle_id", nullable = false)
   
    @JsonIgnoreProperties("resultatsControle") // à adapter selon la relation inverse dans PlanDeControle
    private PlanDeControle planDeControle; // Référence au plan de contrôle

    @ManyToOne
    @JoinColumn(name = "controle_id", nullable = false)
    
    @JsonIgnoreProperties("resultatsControle") // Ignore les resultatsControle dans Controle pour éviter les boucles
    private Controle controle; // Contrôle auquel appartient ce résultat
}