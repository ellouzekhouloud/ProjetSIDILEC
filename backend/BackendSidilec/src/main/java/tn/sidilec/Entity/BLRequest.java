package tn.sidilec.Entity;
import java.time.LocalDate;
import java.util.List;

public class BLRequest {

	private String numBL;
    private Long idFournisseur;
    private LocalDate dateReception;
    private String numClient;
    private String reference;
    private String referenceInterne;
    private String description;
    private List<ProduitRequest> produits;

    
    
	public String getNumBL() {
        return numBL;
    }
    public void setNumBL(String numBL) {
        this.numBL = numBL;
    }
    public Long getIdFournisseur() {
        return idFournisseur;
    }
    public void setIdFournisseur(Long idFournisseur) {
        this.idFournisseur = idFournisseur;
    }
    
    
	public LocalDate getDateReception() {
		return dateReception;
	}
	public void setDateReception(LocalDate dateReception) {
		this.dateReception = dateReception;
	}
	public String getNumClient() {
        return numClient;
    }
    public void setNumClient(String numClient) {
        this.numClient = numClient;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getReferenceInterne() {
        return referenceInterne;
    }
    public void setReferenceInterne(String referenceInterne) {
        this.referenceInterne = referenceInterne;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<ProduitRequest> getProduits() {
        return produits;
    }
    public void setProduits(List<ProduitRequest> produits) {
        this.produits = produits;
    }
}
