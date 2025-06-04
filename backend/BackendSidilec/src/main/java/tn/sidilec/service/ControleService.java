package tn.sidilec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tn.sidilec.Entity.BLProduit;
import tn.sidilec.Entity.Controle;
import tn.sidilec.Entity.PlanDeControle;
import tn.sidilec.Entity.Produit;
import tn.sidilec.Entity.ResultatControle;
import tn.sidilec.Repository.BLProduitRepository;
import tn.sidilec.Repository.ControleRepository;
import tn.sidilec.Repository.PlanDeControleRepository;
import tn.sidilec.Repository.ProduitRepository;


import java.util.List;

@Service
public class ControleService {
    @Autowired
    private ControleRepository controleRepository;
    
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private PlanDeControleRepository planDeControleRepository;
    @Autowired
    private BLProduitRepository blProduitRepository;
    
    @Transactional
    public void enregistrerControle(Controle controle) {
        // Log des données reçues
        System.out.println("Données reçues : " + controle);

        // Vérifier que le produit existe
        Produit produit = produitRepository.findById(controle.getProduit().getIdProduit())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        controle.setProduit(produit);

        // Lier les résultats de contrôle aux plans de contrôle
        for (ResultatControle resultat : controle.getResultatsControle()) {
            PlanDeControle plan = planDeControleRepository.findById(resultat.getPlanDeControle().getId())
                    .orElseThrow(() -> new RuntimeException("Plan de contrôle introuvable"));
            resultat.setPlanDeControle(plan);
            resultat.setControle(controle);
        }
        

        // Valider la quantité
        System.out.println("Quantité avant validation : " + controle.getQuantite());
        if (controle.getQuantite() == null || controle.getQuantite() <= 0) {
            controle.setQuantiteStatus("invalid");
        } else {
            controle.setQuantiteStatus("valid");
        }

        System.out.println("Status de la quantité après validation : " + controle.getQuantiteStatus());

        // Sauvegarde du contrôle avec les résultats
        controleRepository.save(controle);

        // ✅ Mise à jour du statut du BLProduit
        List<BLProduit> blProduits = blProduitRepository.findByProduitIdAndStatus(produit.getIdProduit(), "ready");

        if (!blProduits.isEmpty()) {
            BLProduit blProduit = blProduits.get(0); // on suppose un seul BLProduit "ready" à la fois
            blProduit.setStatus("terminé");
            blProduitRepository.save(blProduit);
         // ✅ Ajouter cette ligne :
            blProduit.setControle(true);
            blProduitRepository.save(blProduit);
            System.out.println("Status du BL_Produit mis à jour en 'terminé'");
        } else {
            System.out.println("Aucun BL_Produit avec le statut 'ready' trouvé pour ce produit.");
        }
        
    }
    
    
    
    public List<Controle> getControles() {
        return controleRepository.findAll();
    }

   
}