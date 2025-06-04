package tn.sidilec.dto;

import java.time.LocalDate;

public class BLEtiquetteVerteDTO {
	private Long id;
    private String numBL;
    private String reference;
    public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	private String fournisseur;
    private String verificateur;
    private LocalDate dateDeControle;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumBL() {
		return numBL;
	}
	public void setNumBL(String numBL) {
		this.numBL = numBL;
	}
	public String getFournisseur() {
		return fournisseur;
	}
	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}
	public String getVerificateur() {
		return verificateur;
	}
	public void setVerificateur(String verificateur) {
		this.verificateur = verificateur;
	}
	public LocalDate getDateDeControle() {
		return dateDeControle;
	}
	public void setDateDeControle(LocalDate dateDeControle) {
		this.dateDeControle = dateDeControle;
	}
	public String getResultat() {
		return resultat;
	}
	public void setResultat(String resultat) {
		this.resultat = resultat;
	}
	private String resultat;
}
