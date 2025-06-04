package tn.sidilec.dto;

import java.util.List;

public class ControleHistoriqueDTO {
	private Long id;
    private String reference;
    private String fournisseur;
    private String verificateur;
    private String numBL;
    private String dateDeControle;
    private String raisonRefus;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNumBL() {
		return numBL;
	}

	public void setNumBL(String numBL) {
		this.numBL = numBL;
	}

	public String getDateDeControle() {
		return dateDeControle;
	}

	public void setDateDeControle(String dateDeControle) {
		this.dateDeControle = dateDeControle;
	}

	public String getRaisonRefus() {
		return raisonRefus;
	}

	public void setRaisonRefus(String raisonRefus) {
		this.raisonRefus = raisonRefus;
	}

	public String getQuantiteStatus() {
		return quantiteStatus;
	}

	public void setQuantiteStatus(String quantiteStatus) {
		this.quantiteStatus = quantiteStatus;
	}

	public List<ResultatControleDTO> getResultatsControle() {
		return resultatsControle;
	}

	public void setResultatsControle(List<ResultatControleDTO> resultatsControle) {
		this.resultatsControle = resultatsControle;
	}

	private String quantiteStatus;
    private List<ResultatControleDTO> resultatsControle;

    // Getters et Setters

    public static class ResultatControleDTO {
        public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getValeurMesuree() {
			return valeurMesuree;
		}
		public void setValeurMesuree(String valeurMesuree) {
			this.valeurMesuree = valeurMesuree;
		}
		public String getVisuel() {
			return visuel;
		}
		public void setVisuel(String visuel) {
			this.visuel = visuel;
		}
		private Long id;
        private String valeurMesuree;
        private String visuel;

        // Getters et Setters
    }
}
