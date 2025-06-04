package tn.sidilec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import tn.sidilec.Entity.BL;
import tn.sidilec.Entity.BLProduit;
import tn.sidilec.Entity.BLRequest;
import tn.sidilec.Entity.BLStatus;
import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Entity.Notification;
import tn.sidilec.Entity.Produit;
import tn.sidilec.Entity.ProduitRequest;
import tn.sidilec.Repository.BLProduitRepository;
import tn.sidilec.Repository.BLRepository;
import tn.sidilec.Repository.FournisseurRepository;
import tn.sidilec.Repository.NotificationRepository;
import tn.sidilec.Repository.ProduitRepository;
import tn.sidilec.dto.BLProduitDto;
import tn.sidilec.dto.ProduitDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@Service
public class BLService {

    @Autowired
    private BLRepository blRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private BLProduitRepository blProduitRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private WebSocketNotificationService wsNotificationService;
    
    

    public BL createBL(BLRequest blRequest) {
    	
    	  // Vérifier si le numBL existe déjà
    	if (blRepository.existsByNumBL(blRequest.getNumBL())) {
    	    throw new ResponseStatusException(
    	        HttpStatus.BAD_REQUEST,
    	        "Ce numéro de bon de livraison existe déjà. Veuillez en saisir un autre."
    	    );
    	}
        // Récupération du fournisseur
        Fournisseur fournisseur = fournisseurRepository.findById(blRequest.getIdFournisseur())
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        // Création du bon de livraison
        BL bl = new BL();
        bl.setNumBL(blRequest.getNumBL());
        bl.setDateReception(blRequest.getDateReception());  
        bl.setNumClient(blRequest.getNumClient());  
        bl.setReference(blRequest.getReference());  
        bl.setReferenceInterne(blRequest.getReferenceInterne()); 
        bl.setDescription(blRequest.getDescription());  
        bl.setFournisseur(fournisseur);

        // Ajout des produits
        List<BLProduit> produits = new ArrayList<>();
        for (ProduitRequest produitRequest : blRequest.getProduits()) {
            Produit produit = produitRepository.findById(produitRequest.getIdProduit())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));

            BLProduit blProduit = new BLProduit();
            blProduit.setBl(bl);
            blProduit.setProduit(produit);
            blProduit.setQuantite(produitRequest.getQuantité());
            blProduit.setStatus("ready");
            produits.add(blProduit);
        }

        bl.setProduits(produits);

       
        BL savedBL = blRepository.save(bl);

        // ✅ Création de la notification pour le contrôleur
        Notification notif = new Notification();
        notif.setMessage("Nouveau bon de livraison à contrôler : " + savedBL.getNumBL());
        notif.setBl(savedBL);
        notif.setRole("CONTROLEUR");
        notif.setRead(false);
        notif.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notif);
     // ✅ ENVOI TEMPS RÉEL via WebSocket
        wsNotificationService.notifyControleurs(notif.getMessage());
        System.out.println("ID du BL sauvegardé : " + savedBL.getId());
        return savedBL;
        
    }
    
    
    ////////////
    public List<BLProduitDto> getBLProduitsByReferenceAndStatus(String reference, String status) {
        // 1. Récupérer le produit par sa référence
        Produit produit = produitRepository.findByReference(reference)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec la référence : " + reference));

        // 2. Récupérer tous les BLProduit pour ce produit avec le status
        List<BLProduit> blProduits = blRepository.findByProduitIdAndStatus(produit.getIdProduit(), status);

     
        List<BLProduitDto> response = new ArrayList<>();
        for (BLProduit bp : blProduits) {
            BL bl = bp.getBl();
            response.add(new BLProduitDto(
                bl.getId(),
                bl.getNumBL(),
                bl.getDateReception(),
                bp.getQuantite(),
                status,
                bp.getProduit().getIdProduit()
            ));
        }

        return response;
    }
    
    /////////////////////
    /*public BL terminerBL(Long idBL) {
        BL bl = blRepository.findById(idBL)
            .orElseThrow(() -> new RuntimeException("BL introuvable avec l'ID : " + idBL));
        
        bl.setTermine(true);
        return blRepository.save(bl);
    }*/
    
    public List<BL> getBLsNonTermines() {
        return blRepository.findByTermineFalse();
    }
    
    public Optional<BL> getBlById(Long id) {
        return blRepository.findById(id);
    }
    
    public boolean tousLesProduitsControles(Long blId) {
    	List<BLProduit> lignes = blProduitRepository.findByBlId(blId);
        return lignes.stream().allMatch(BLProduit::isControle);
    }
    
    public BL updateBL(BL bl) {
        return blRepository.save(bl);
    }
    
    public List<ProduitDTO> getProduitsDuBL(Long blId) {
        BL bl = blRepository.findById(blId)
                .orElseThrow(() -> new RuntimeException("Bon de livraison introuvable"));

        return bl.getProduits().stream()
                .map(blProduit -> {
                    Produit produit = blProduit.getProduit();
                    ProduitDTO dto = new ProduitDTO();
                    dto.setReference(produit.getReference());
                    dto.setDesignation(produit.getNom());
                    dto.setFournisseurNom(produit.getFournisseur().getNomFournisseur());
                    dto.setQuantite(blProduit.getQuantite());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public boolean verifierSiBLTermine(Long blId) {
        BL bl = blRepository.findById(blId)
                .orElseThrow(() -> new RuntimeException("BL non trouvé avec l’ID : " + blId));

        // Vérifier si tous les produits sont contrôlés
        boolean tousControles = bl.getProduits().stream()
                .allMatch(blProduit -> blProduit.isControle());

        // Log pour débogage
        System.out.println("Tous les contrôles effectués : " + tousControles);

        if (tousControles && bl.getStatus() != BLStatus.TERMINE) {
            bl.setStatus(BLStatus.TERMINE);  // Mettre à jour le statut du BL
            bl.setTermine(true);             // Mettre à jour le champ 'termine'
            blRepository.save(bl);           // Sauvegarder les modifications
        }

        return tousControles;
    }
    
    /////marquer produit comme controlé 
    public void marquerProduitCommeControle(Long blProduitId) {
        BLProduit blProduit = blProduitRepository.findById(blProduitId)
            .orElseThrow(() -> new RuntimeException("Produit du BL non trouvé"));

        blProduit.setControle(true); // is_controle = true
        blProduitRepository.save(blProduit);
    }
    
    public List<BLProduit> getProduitsControlesParBL(Long blId) {
        return blProduitRepository.findByBlIdAndIsControleTrue(blId);
    }
    
    public List<BL> getAllBL() {
        return blRepository.findAll();
    }
    @Transactional
    public void deleteBL(Long id) {
    	notificationRepository.deleteByBlId(id);
        blRepository.deleteById(id);
    }
    
   
  

}
