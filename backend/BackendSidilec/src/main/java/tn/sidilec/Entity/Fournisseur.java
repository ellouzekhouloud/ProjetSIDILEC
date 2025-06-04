package tn.sidilec.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

@NoArgsConstructor
@ToString // ✅ Lombok génère toString(), mais sans les produits
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFournisseur;

    private String nomFournisseur;
    private String certificat;
    private String email;
    private String adresse;
    private String telephone;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ✅ Évite la récursion infinie
    @ToString.Exclude // ✅ Exclut la liste des produits du `toString()`
    private List<Produit> produits = new ArrayList<>();
}
    

