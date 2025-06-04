package tn.sidilec.dto;

import lombok.Data;

@Data
public class ProduitDTO {
	 private String reference;
	    private String designation;
	    private String fournisseurNom;
	    private int quantite;
}
