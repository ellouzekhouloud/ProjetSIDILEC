package tn.sidilec.Controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import tn.sidilec.Entity.BL;
import tn.sidilec.Entity.BLProduit;
import tn.sidilec.Entity.BLRequest;
import tn.sidilec.Entity.BLStatus;

import tn.sidilec.Repository.BLProduitRepository;
import tn.sidilec.Repository.BLRepository;
import tn.sidilec.dto.BLProduitDto;
import tn.sidilec.dto.ProduitDTO;
import tn.sidilec.service.BLService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bl")
@CrossOrigin(origins = "http://localhost:4200")
public class BLController {

    @Autowired
    private BLService blService;
    @Autowired
    private BLProduitRepository bLProduitRepository;
    @Autowired
    private BLRepository blRepository;
    

    @PostMapping("/create")
    public ResponseEntity<BL> createBL(@RequestBody BLRequest blRequestDto) {
        BL newBL = blService.createBL(blRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBL);
    }
    
    @GetMapping("/produit/{reference}/bls")
    public ResponseEntity<List<BLProduitDto>> getBLsByProduitReference(
            @PathVariable String reference,
            @RequestParam(defaultValue = "ready") String status) {
        List<BLProduitDto> result = blService.getBLProduitsByReferenceAndStatus(reference, status);
        return ResponseEntity.ok(result);
    }
    
    /*@PutMapping("/{id}/terminer")
    public ResponseEntity<BL> terminerBL(@PathVariable Long id) {
        BL updatedBL = blService.terminerBL(id);
        return ResponseEntity.ok(updatedBL);
    }*/
    
    @GetMapping("/non-termines")
    public ResponseEntity<List<BL>> getBLsNonTermines() {
        List<BL> bls = blService.getBLsNonTermines();
        return ResponseEntity.ok(bls);
    }
    
    
    ////16/04/2025
    @PutMapping("/ligne/{id}/controle")
    public ResponseEntity<?> marquerCommeControle(@PathVariable Long id) {
        BLProduit ligne = bLProduitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ligne non trouvée"));

        ligne.setControle(true);  
        bLProduitRepository.save(ligne);

        BL bl = ligne.getBl();  
        boolean tousControles = blService.tousLesProduitsControles(bl.getId());

        if (tousControles) {
            bl.setStatus(BLStatus.TERMINE); 
            //bl.setTermine(true);        

            blService.updateBL(bl);  
        }

        return ResponseEntity.ok("Produit contrôlé. Mise à jour du BL si nécessaire.");
    }
    
    @GetMapping("/{id}/produits-controles")
    public ResponseEntity<List<BLProduit>> getProduitsControlesParBL(@PathVariable Long id) {
        List<BLProduit> produitsControles = blService.getProduitsControlesParBL(id);
        return ResponseEntity.ok(produitsControles);
    }
    
    @GetMapping("/{id}/produits")
    public List<BLProduit> getProduitsByBL(@PathVariable Long id) {
        BL bl = blRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BL non trouvé"));

        return bl.getProduits();
    }
    
    
    @PutMapping("/produits/{id}/controler")
    public ResponseEntity<?> controlerProduit(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<BLProduit> optional = bLProduitRepository.findById(id);
        if (optional.isPresent()) {
            BLProduit produit = optional.get();
            produit.setControleEffectue(true); // ✅ Marquer comme contrôlé

            // Si besoin, enregistrer fiche de refus ou conforme depuis le payload
            //produit.setStatutControle((String) payload.get("statutControle")); // "Conforme" ou "Refus"

            bLProduitRepository.save(produit);
            return ResponseEntity.ok("Produit contrôlé avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé");
        }
    }
    
    @GetMapping("/{blId}/produits")
    public ResponseEntity<List<ProduitDTO>> getProduitsDuBL(@PathVariable Long blId) {
        List<ProduitDTO> produits = blService.getProduitsDuBL(blId);
        return ResponseEntity.ok(produits);
    }
   
    
    @PutMapping("/{id}/terminer")
    public ResponseEntity<Boolean> terminerBL(@PathVariable Long id) {
        boolean estTermine = blService.verifierSiBLTermine(id);

        return ResponseEntity.ok(estTermine);
    }
    
    ////marquer produit comme controlé 
    @PutMapping("/produits/{id}/controle")
    public ResponseEntity<?> marquerControleProduit(@PathVariable Long id) {
        blService.marquerProduitCommeControle(id);
        return ResponseEntity.ok("Produit marqué comme contrôlé");
    }
    
    @GetMapping("{id}")
    public ResponseEntity<?> getBlById(@PathVariable Long id) {
        Optional<BL> bl = blService.getBlById(id);

        if (bl.isPresent()) {
            return ResponseEntity.ok(bl.get()); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
    
    @GetMapping("/{blId}/produits-controles-details")
    public List<String> getProduitsControles(@PathVariable Long blId) {
        // Log pour vérifier que l'ID de BL est correct
        System.out.println("Récupération des produits contrôlés pour le BL ID : " + blId);

        List<BLProduit> blProduits = bLProduitRepository.findByBlIdAndIsControleTrue(blId);
        
        // Log pour vérifier le nombre de produits récupérés
        System.out.println("Nombre de produits contrôlés trouvés : " + blProduits.size());

        // Retourner une liste des références des produits
        return blProduits.stream()
                .map(blProduit -> blProduit.getProduit().getReference())  // Récupérer la référence du produit
                .collect(Collectors.toList());
    }
    
    @GetMapping
    public List<BL> getAllBL() {
        return blService.getAllBL();
    }
    @DeleteMapping("/{idBL}")
    public void deleteBL(@PathVariable Long idBL) {
        blService.deleteBL(idBL);    
    }
    
  


    
}
