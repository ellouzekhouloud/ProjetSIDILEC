package tn.sidilec.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Entity.Produit;
import tn.sidilec.service.FournisseurService;
import tn.sidilec.service.ProduitService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/fournisseurs", produces = "application/json")

public class FournisseurController {
	@Autowired
    private FournisseurService fournisseurService;
	
	@Autowired
    private ProduitService produitService;

    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs();
    }

    @GetMapping("/{id}")
    public Optional<Fournisseur> getFournisseurById(@PathVariable Long id) {
        return fournisseurService.getFournisseurById(id);
    }

    @PostMapping
    public ResponseEntity<Fournisseur> addFournisseur(@RequestBody Fournisseur fournisseur) {
        Fournisseur newFournisseur = fournisseurService.addFournisseur(fournisseur);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFournisseur);
    }

    @PutMapping("/{id}")
    public Fournisseur updateFournisseur(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        return fournisseurService.updateFournisseur(id, fournisseur);
    }

    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
    }
    
    @GetMapping("/fournisseur/{id}/produits")
    public ResponseEntity<List<Produit>> getProduitsByFournisseur(@PathVariable Long id) {
        List<Produit> produits = produitService.getProduitsByFournisseur(id);  // Utilisation de l'instance produitService
        if (produits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(produits);
    }
    
}
