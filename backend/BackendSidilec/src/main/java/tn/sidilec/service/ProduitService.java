package tn.sidilec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.Produit;
import tn.sidilec.Repository.ProduitRepository;

@Service
public class ProduitService {
    @Autowired
    private ProduitRepository produitRepository;

    
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
    
    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public List<Produit> getProduitsByFournisseur(Long fournisseurId) {
        return produitRepository.findByFournisseur_IdFournisseur(fournisseurId);
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
    
    
    public Produit editProduit(Long id, Produit produitUpdated) {
        System.out.println("Début editProduit pour id = " + id);
        System.out.println("Données reçues :");
        System.out.println("reference = " + produitUpdated.getReference());
        System.out.println("nom = " + produitUpdated.getNom());
        System.out.println("moq = " + produitUpdated.getMoq());
        System.out.println("imagePath = " + produitUpdated.getImagePath());
        System.out.println("ficheTechniquePath = " + produitUpdated.getFicheTechniquePath());
        System.out.println("fournisseur = " + (produitUpdated.getFournisseur() != null ? produitUpdated.getFournisseur().getIdFournisseur() : "null"));
        System.out.println("famille = " + (produitUpdated.getFamille() != null ? produitUpdated.getFamille().getIdFamille() : "null"));

        return produitRepository.findById(id).map(produit -> {
            produit.setReference(produitUpdated.getReference());
            produit.setNom(produitUpdated.getNom());
            produit.setMoq(produitUpdated.getMoq());

            if (produitUpdated.getFournisseur() != null) {
                produit.setFournisseur(produitUpdated.getFournisseur());
            }
            if (produitUpdated.getFamille() != null) {
                produit.setFamille(produitUpdated.getFamille());
            }

            if (produitUpdated.getImagePath() != null && !produitUpdated.getImagePath().isEmpty()) {
                System.out.println("Mise à jour imagePath : " + produitUpdated.getImagePath());
                produit.setImagePath(produitUpdated.getImagePath());
            } else {
                System.out.println("imagePath non modifié");
            }

            if (produitUpdated.getFicheTechniquePath() != null && !produitUpdated.getFicheTechniquePath().isEmpty()) {
                System.out.println("Mise à jour ficheTechniquePath : " + produitUpdated.getFicheTechniquePath());
                produit.setFicheTechniquePath(produitUpdated.getFicheTechniquePath());
            } else {
                System.out.println("ficheTechniquePath non modifié");
            }

            Produit savedProduit = produitRepository.save(produit);
            System.out.println("Produit mis à jour sauvegardé avec succès");
            return savedProduit;
        }).orElseThrow(() -> new RuntimeException("Produit non trouvé avec id : " + id));
    }

    
}