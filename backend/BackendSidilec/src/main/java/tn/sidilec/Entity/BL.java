package tn.sidilec.Entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "bls")
public class BL {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true)
	private String numBL;
	
    private LocalDate dateReception;
    private String numClient;
    private String reference; 
    private String referenceInterne; 
    private String description; 
    private boolean termine = false;
    
    @Enumerated(EnumType.STRING)
    private BLStatus status = BLStatus.PRET;  // ✅ Status par défaut
    
    @ManyToOne
    @JoinColumn(name = "id_fournisseur", nullable = false)
    private Fournisseur fournisseur;
    
    @OneToMany(mappedBy = "bl", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BLProduit> produits = new ArrayList<>();
}
