package tn.sidilec.Entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Famille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFamille;

    private String nomFamille;
    
 
    @OneToMany(mappedBy = "famille")
    private List<Produit> produits;
   
    public Long getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(Long idFamille) {
        this.idFamille = idFamille;
    }

    public String getNomFamille() {
        return nomFamille;
    }

    public void setNomFamille(String nomFamille) {
        this.nomFamille = nomFamille;
    }
}
