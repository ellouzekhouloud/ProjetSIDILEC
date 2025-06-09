package tn.sidilec.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.sidilec.Entity.BL;
import tn.sidilec.Entity.BLProduit;
import tn.sidilec.Entity.Controle;


import tn.sidilec.Repository.BLRepository;
import tn.sidilec.Repository.ControleRepository;
import tn.sidilec.service.ControleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/controles")
@CrossOrigin("*")
public class ControleController {
    @Autowired
    private ControleService controleService;

    @Autowired
    private BLRepository blRepository;
    @Autowired
    private ControleRepository controleRepository ;
    
    @PostMapping("/enregistrer")
    public ResponseEntity<String> enregistrerControle(@RequestBody Controle controle) {
        try {
            System.out.println("🟢 Reçu : " + controle); // ou utilise logger
            controleService.enregistrerControle(controle);
            return ResponseEntity.ok("Contrôle enregistré avec succès !");
        } catch (Exception e) {
            e.printStackTrace(); // pour voir l'erreur complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }

    
    
   
    
    @GetMapping("/{id}/controle-effectue")
    public ResponseEntity<Boolean> isBLControleEffectue(@PathVariable Long id) {
        Optional<BL> blOptional = blRepository.findById(id);
        if (blOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BL bl = blOptional.get();
        List<BLProduit> produits = bl.getProduits();

        for (BLProduit bp : produits) {
            Long produitId = bp.getProduit().getIdProduit();
            String numBL = bl.getNumBL();

            // Vérifie s'il existe un contrôle pour ce produit et ce BL
            Controle controle = controleRepository.findByProduit_IdProduitAndNumBL(produitId, numBL);

            if (controle == null) {
                return ResponseEntity.ok(false); // Ce produit n'a pas encore été contrôlé
            }
        }

        return ResponseEntity.ok(true); // Tous les produits du BL ont bien été contrôlés
    }
    
    @GetMapping("/tous")
    public List<Controle> getTousLesControles() {
        return controleService.getControles();
    }
    
   
}