package tn.sidilec.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Repository.BLProduitRepository;

import tn.sidilec.Repository.FamilleRepository;

import tn.sidilec.Repository.FournisseurRepository;
import tn.sidilec.Repository.ProduitRepository;
import tn.sidilec.dto.BLEtiquetteVerteDTO;
import tn.sidilec.dto.BLFicheDeRefusDTO;
import tn.sidilec.dto.QuantiteFournisseurDTO;
import tn.sidilec.dto.StatFournisseurDto;
import tn.sidilec.service.DashboardService;
import tn.sidilec.service.EtiquetteVerteService;
import tn.sidilec.service.FicheDeRefusService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private FamilleRepository familleRepository;
	@Autowired
	private DashboardService dashboardService;
	

	
	@Autowired
	private EtiquetteVerteService etiquetteVerteService;
	@Autowired
	private FicheDeRefusService ficheDeRefusService;
	@Autowired
	private BLProduitRepository blProduitRepository;

	@GetMapping("/stats")
	public Map<String, Long> getGlobalStats() {
		Map<String, Long> stats = new HashMap<>();
		stats.put("totalProduits", produitRepository.count());
		stats.put("totalFournisseurs", fournisseurRepository.count());
		stats.put("totalFamilles", familleRepository.count());
		return stats;
	}

	// ✅ Nouvelle méthode pour récupérer les produits par fournisseur
	@GetMapping("/produits-par-fournisseur")
	public List<Map<String, Object>> getProduitsParFournisseur() {
		List<Map<String, Object>> results = new ArrayList<>();

		fournisseurRepository.findAll().forEach(fournisseur -> {
			Map<String, Object> data = new HashMap<>();
			data.put("fournisseur", fournisseur.getNomFournisseur());
			data.put("nombreProduits", produitRepository.countByFournisseur(fournisseur));
			results.add(data);
		});

		return results;
	}
	
	
	// Produits non conformes, nombre de BL , PPm par fournisseurs
	@GetMapping("/non-conformes-par-fournisseur")
	public List<Map<String, Object>> getNonConformesParFournisseur(
	        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

	    Map<String, StatFournisseurDto> stats = new HashMap<>();

	    // Requête 1 : Fiches de refus
	    List<Map<String, Object>> refusList = produitRepository.countNonConformesByFournisseur(startDate, endDate);
	    for (Map<String, Object> row : refusList) {
	        String fournisseur = (String) row.get("fournisseur");
	        Long nonConformes = ((Number) row.get("nonConformes")).longValue();

	        stats.putIfAbsent(fournisseur, new StatFournisseurDto(fournisseur));
	        stats.get(fournisseur).setNonConformes(nonConformes);
	    }

	    // Requête 2 : Nombre de BL
	    List<Map<String, Object>> blList = produitRepository.countBLByFournisseurFromControle(startDate, endDate);
	    for (Map<String, Object> row : blList) {
	        String fournisseur = (String) row.get("fournisseur");
	        Long nombreBL = ((Number) row.get("nombreBL")).longValue();

	        stats.putIfAbsent(fournisseur, new StatFournisseurDto(fournisseur));
	        stats.get(fournisseur).setNombreBL(nombreBL);
	    }

	    // Convertir en List<Map<String, Object>> pour la réponse
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (StatFournisseurDto dto : stats.values()) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("fournisseur", dto.getFournisseur());
	        map.put("nonConformes", dto.getNonConformes());
	        map.put("nombreBL", dto.getNombreBL());

	        // ✅ Calcul PPM
	        Long nonConformes = dto.getNonConformes() != null ? dto.getNonConformes() : 0L;
	        Long nombreBL = dto.getNombreBL() != null ? dto.getNombreBL() : 0L;
	        double ppm = (nombreBL != 0) ? ((double) nonConformes / nombreBL) * 1_000_000 : 0;

	        map.put("ppm", ppm);

	        result.add(map);
	    }
	    result.sort((map1, map2) -> {
	        Long nc1 = ((Number) map1.get("nonConformes")).longValue();
	        Long nc2 = ((Number) map2.get("nonConformes")).longValue();
	        return Long.compare(nc2, nc1);
	    });
	    return result;
	    
	}


	// quantités par fournisseur + non conformes
	@GetMapping("/quantites-par-fournisseur")
	public List<QuantiteFournisseurDTO> getQuantitesParFournisseur(
	        @RequestParam("filterType") String filterType,
	        @RequestParam(value = "date", required = false) String date,
	        @RequestParam(value = "startDate", required = false) String startDateStr,
	        @RequestParam(value = "endDate", required = false) String endDateStr) {

	    return dashboardService.getQuantitesParFournisseur(filterType, date, startDateStr, endDateStr);
	}


	// Produit non conforme sur produit receptionné
	@GetMapping("/ppm")
	public double getTauxNonConformite(@RequestParam String fournisseur) {
	    Long totalFichesRefus = blProduitRepository.countFichesDeRefusByFournisseur(fournisseur);
	    Long totalEtiquettes = blProduitRepository.countEtiquettesVertesByFournisseur(fournisseur);

	    totalFichesRefus = totalFichesRefus != null ? totalFichesRefus : 0L;
	    totalEtiquettes = totalEtiquettes != null ? totalEtiquettes : 0L;

	    long totalProduits = totalFichesRefus + totalEtiquettes;

	    return totalProduits > 0 ? ((double) totalFichesRefus / totalProduits) * 1_000_000 : 0 ;
	}

	

	// les bl conformes 
	@GetMapping("/traites-succes")
	public ResponseEntity<List<BLEtiquetteVerteDTO>> getBlsTraitesAvecSucces(
			@RequestParam(required = false) String fournisseur,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<BLEtiquetteVerteDTO> bls = etiquetteVerteService.getBlsAvecEtiquettesVertes(fournisseur, date);
		return ResponseEntity.ok(bls);
	}
	
	// ✅ Endpoint pour les BL non conformes
    @GetMapping("/non-conformes")
    public ResponseEntity<List<BLFicheDeRefusDTO>> getBlsNonConformes(
            @RequestParam(required = false) String fournisseur,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BLFicheDeRefusDTO> bls = ficheDeRefusService.getBlsNonConformes(fournisseur, date);
        return ResponseEntity.ok(bls);
    }
  
   
}
