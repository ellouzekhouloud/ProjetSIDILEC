package tn.sidilec.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;



@Entity
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;
    
    @Column(unique = true, nullable = false)
    private String reference;
    
    private String nom;
    //private String description;
    //private double prix;
    private String imagePath;
    private String ficheTechniquePath;

    private double moq;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @ToString.Exclude // ✅ Exclut fournisseur du `toString()`
    @JsonProperty("fournisseur")
    private Fournisseur fournisseur;
    
    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PlanDeControle> plansDeControle;
    
    
}