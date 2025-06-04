package tn.sidilec.dto;

public class StatFournisseurDto {
	private String fournisseur;
    private Long nonConformes = 0L;
    private Long nombreBL = 0L;

    public StatFournisseurDto(String fournisseur) {
        this.fournisseur = fournisseur;
    }

	public String getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}

	public Long getNonConformes() {
		return nonConformes;
	}

	public void setNonConformes(Long nonConformes) {
		this.nonConformes = nonConformes;
	}

	public Long getNombreBL() {
		return nombreBL;
	}

	public void setNombreBL(Long nombreBL) {
		this.nombreBL = nombreBL;
	}
    
}
