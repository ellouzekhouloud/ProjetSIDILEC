package tn.sidilec.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Entity.PlanDeControle;
import tn.sidilec.Entity.PlanDeControleRequest;
import tn.sidilec.Entity.Produit;
import tn.sidilec.service.PlanDeControleService;

@RestController
@RequestMapping("/api/plans-de-controle")
@CrossOrigin(origins = "http://localhost:4200")
public class PlanDeControleController {

    @Autowired
    private PlanDeControleService planDeControleService;

    @PostMapping("/ajouter")
    public List<PlanDeControle> ajouterPlanDeControle(@RequestBody PlanDeControleRequest planRequest) {
        
        Long idProduit = planRequest.getIdProduit();
        List<PlanDeControle> lignes = planRequest.getLignes();

        // Associe chaque ligne de plan à un produit
        for (PlanDeControle ligne : lignes) {
            Produit produit = new Produit();
            produit.setIdProduit(idProduit); 
            ligne.setProduit(produit); 
        }

       
        return planDeControleService.ajouterPlansDeControle(lignes);
    }

    @GetMapping("/produit/{produitId}")
    public List<PlanDeControle> getPlansByProduit(@PathVariable Long produitId) {
        return planDeControleService.getPlansByProduit(produitId);
    }

    @PutMapping("/modifier/{id}")
    public PlanDeControle modifierPlanDeControle(@PathVariable Long id, @RequestBody PlanDeControle planDetails) {
        return planDeControleService.modifierPlanDeControle(id, planDetails);
    }

    @DeleteMapping("/supprimer/{id}")
    public void supprimerPlanDeControle(@PathVariable Long id) {
        planDeControleService.supprimerPlanDeControle(id);
    }
}
