package tn.sidilec.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bl_produits")
public class BLProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private int quantite;
	private String status;
	private boolean controleTermine ;
	private boolean isControle = false;
	private boolean controleEffectue;
	
	@ManyToOne
    @JoinColumn(name = "bl_id", nullable = false)
	@JsonBackReference
    private BL bl;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
    
}
