package tn.sidilec.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import tn.sidilec.Entity.Famille;
import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Entity.PlanDeControle;
import tn.sidilec.Entity.Produit;
import tn.sidilec.Repository.FamilleRepository;
import tn.sidilec.Repository.FournisseurRepository;
import tn.sidilec.Repository.ProduitRepository;
import tn.sidilec.service.ProduitService;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:4200")
public class ProduitController {
    @Autowired
    private ProduitService produitService;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private FamilleRepository familleRepository;

    @Autowired
    private ProduitRepository produitRepository;
    private final Path IMAGE_UPLOAD_DIR = Paths.get("/app/uploads/images");
    private final Path FICHE_UPLOAD_DIR = Paths.get("/app/uploads/fiche_technique");

    @GetMapping("/all")
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addProduit(@RequestBody Produit produit) {
        System.out.println("Produit reçu par le backend : " + produit);

        // Vérifier si le fournisseur est présent et son ID
        if (produit.getFournisseur() == null || produit.getFournisseur().getIdFournisseur() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : Le fournisseur est null ou son ID est manquant !");
        }

        // Vérifier l'existence du fournisseur
        Optional<Fournisseur> fournisseurOpt = fournisseurRepository.findById(produit.getFournisseur().getIdFournisseur());
        if (!fournisseurOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : Le fournisseur avec ID " + produit.getFournisseur().getIdFournisseur() + " n'existe pas.");
        }

        // Associer le fournisseur trouvé au produit
        produit.setFournisseur(fournisseurOpt.get());

        // Vérifier si la famille est présente et son ID
        if (produit.getFamille() == null || produit.getFamille().getIdFamille() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : La famille est null ou son ID est manquant !");
        }

        // Vérifier l'existence de la famille
        Optional<Famille> familleOpt = familleRepository.findById(produit.getFamille().getIdFamille());
        if (!familleOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : La famille avec ID " + produit.getFamille().getIdFamille() + " n'existe pas.");
        }

        // Associer la famille trouvée au produit
        produit.setFamille(familleOpt.get());

        

        // Sauvegarder le produit
        Produit savedProduit = produitService.ajouterProduit(produit);

        // Retourner le produit sauvegardé
        return ResponseEntity.ok(savedProduit);
    }


    @GetMapping("/fournisseur/{id}")
    public List<Produit> getProduitsByFournisseur(@PathVariable Long id) {
        return produitService.getProduitsByFournisseur(id);
    }

    @GetMapping("/{id}")
    public Optional<Produit> getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id);
    }
    
    @GetMapping("/reference/{reference}")
    public ResponseEntity<Produit> getProduitByReference(@PathVariable String reference) {
        Optional<Produit> produitOpt = produitRepository.findByReference(reference);
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            // Récupérer le produit avec ses plans de contrôle associés
            return ResponseEntity.ok(produit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/{idProduit}/plansDeControle")
    public ResponseEntity<List<PlanDeControle>> getPlansDeControleByProduit(@PathVariable Long idProduit) {
        Optional<Produit> produitOpt = produitRepository.findById(idProduit);
        if (produitOpt.isPresent()) {
            List<PlanDeControle> plansDeControle = produitOpt.get().getPlansDeControle();
            return ResponseEntity.ok(plansDeControle);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @GetMapping("/{reference}/plan-de-controle")
    public ResponseEntity<List<PlanDeControle>> getPlanDeControleByReference(@PathVariable String reference) {
      Optional   <Produit> produitOpt = produitRepository.findByReference(reference);
      if (produitOpt.isPresent()) {
          List<PlanDeControle> plansDeControle = produitOpt.get().getPlansDeControle();
          return ResponseEntity.ok(plansDeControle);
      } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    }

    @DeleteMapping("/delete/{id}")
    public void supprimerProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
    }
    
    @PostMapping("/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
           if (!Files.exists(IMAGE_UPLOAD_DIR)) {
    Files.createDirectories(IMAGE_UPLOAD_DIR);
}
Path filePath = IMAGE_UPLOAD_DIR.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imagePath = "/images/" + fileName;
            
           
            System.out.println("Image Path: " + imagePath);
            
            // ✅ Retourner un JSON valide
            Map<String, String> response = new HashMap<>();
            response.put("imagePath", imagePath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Échec de l'upload"));
        }
    }

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = IMAGE_UPLOAD_DIR.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Adaptez selon le type d'image
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    //Upload d'une fiche technique
@PostMapping("/uploadFicheTechnique")
public ResponseEntity<Map<String, String>> uploadFicheTechnique(@RequestParam("file") MultipartFile file) {
    try {
        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Créer le dossier s'il n'existe pas
        if (!Files.exists(FICHE_UPLOAD_DIR)) {
            Files.createDirectories(FICHE_UPLOAD_DIR);
        }

        // Chemin complet du fichier
        Path filePath = FICHE_UPLOAD_DIR.resolve(fileName);

        // Copier le fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Retourner le chemin relatif ou complet si besoin
        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("ficheTechniquePath", "/fiche_technique/" + fileName);

        return ResponseEntity.ok(response);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Erreur lors de l'upload du fichier"));
    }
}

    
    @GetMapping("/ficheTechnique/{filename:.+}")
    public ResponseEntity<Resource> getFicheTechnique(@PathVariable String filename) {
        try {
            Path filePath = FICHE_UPLOAD_DIR.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
