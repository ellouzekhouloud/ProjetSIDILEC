package tn.sidilec.dto;

import java.time.LocalDate;

public class BLFicheDeRefusDTO {
	 private Long id;
	    private String numBL;
	    private String reference;
	    private String fournisseur;
	    private String verificateur;
	    private LocalDate dateDeControle;
	    private String motifRefus;
	    private String raison;
	    private Integer quantiteIncorrecte;
	    
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
		public String getReference() {
			return reference;
		}
		public void setReference(String reference) {
			this.reference = reference;
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
		public String getMotifRefus() {
			return motifRefus;
		}
		public void setMotifRefus(String motifRefus) {
			this.motifRefus = motifRefus;
		}
		public String getRaison() {
			return raison;
		}
		public void setRaison(String raison) {
			this.raison = raison;
		}
		public Integer getQuantiteIncorrecte() {
			return quantiteIncorrecte;
		}
		public void setQuantiteIncorrecte(Integer quantiteIncorrecte) {
			this.quantiteIncorrecte = quantiteIncorrecte;
		}
		
}
