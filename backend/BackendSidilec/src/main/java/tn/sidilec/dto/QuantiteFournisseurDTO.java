package tn.sidilec.dto;

public class QuantiteFournisseurDTO {
    private String nomFournisseur;
    private Long quantiteReceptionnee;
    private Long quantiteNonConforme;

    // Constructeur
    public QuantiteFournisseurDTO(String nomFournisseur, Long quantiteReceptionnee, Long quantiteNonConforme) {
        this.nomFournisseur = nomFournisseur;
        this.quantiteReceptionnee = quantiteReceptionnee;
        this.quantiteNonConforme = quantiteNonConforme;
    }

    // Getters et Setters
    public String getNomFournisseur() {
        return nomFournisseur;
    }

    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }

    public Long getQuantiteReceptionnee() {
        return quantiteReceptionnee;
    }

    public void setQuantiteReceptionnee(Long quantiteReceptionnee) {
        this.quantiteReceptionnee = quantiteReceptionnee;
    }

    public Long getQuantiteNonConforme() {
        return quantiteNonConforme;
    }

    public void setQuantiteNonConforme(Long quantiteNonConforme) {
        this.quantiteNonConforme = quantiteNonConforme;
    }
}
