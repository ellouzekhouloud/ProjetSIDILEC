package tn.sidilec.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tn.sidilec.Entity.EtiquetteVerte;
import tn.sidilec.Repository.EtiquetteVerteRepository;
import tn.sidilec.dto.BLEtiquetteVerteDTO;
@Service
public class EtiquetteVerteService {
	private final EtiquetteVerteRepository etiquetteVerteRepository;

    public EtiquetteVerteService(EtiquetteVerteRepository etiquetteVerteRepository) {
        this.etiquetteVerteRepository = etiquetteVerteRepository;
    }

    public EtiquetteVerte creerEtiquette(EtiquetteVerte etiquette) {
        return etiquetteVerteRepository.save(etiquette);
    }
    
    // les bl conformes
    public List<BLEtiquetteVerteDTO> getBlsAvecEtiquettesVertes(String fournisseur, LocalDate date) {
        List<EtiquetteVerte> etiquettes = etiquetteVerteRepository.findEtiquettesVertesByFilters(fournisseur, date);

        return etiquettes.stream().map(e -> {
            BLEtiquetteVerteDTO dto = new BLEtiquetteVerteDTO();
            dto.setId(e.getId());
            dto.setNumBL(e.getNumBL());
            dto.setReference(e.getReference());
            dto.setFournisseur(e.getFournisseur());
            dto.setVerificateur(e.getVerificateur());
            dto.setDateDeControle(e.getDateDeControle());
            dto.setResultat(e.getResultat());
            return dto;
        }).collect(Collectors.toList());
    }
    
    
}
