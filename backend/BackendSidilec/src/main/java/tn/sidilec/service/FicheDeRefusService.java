package tn.sidilec.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tn.sidilec.Entity.FicheDeRefus;
import tn.sidilec.Repository.FicheDeRefusRepository;
import tn.sidilec.dto.BLFicheDeRefusDTO;

@Service
public class FicheDeRefusService {

    private final FicheDeRefusRepository ficheDeRefusRepository;

    public FicheDeRefusService(FicheDeRefusRepository ficheDeRefusRepository) {
        this.ficheDeRefusRepository = ficheDeRefusRepository;
    }

    @Transactional
    public FicheDeRefus creerFiche(FicheDeRefus fiche) {
        Long maxNumero = ficheDeRefusRepository.findMaxNumeroFiche();
        fiche.setNumeroFiche((maxNumero != null ? maxNumero : 0) + 1);
        return ficheDeRefusRepository.save(fiche);
    }
    
    // bl non-conforme 
    public List<BLFicheDeRefusDTO> getBlsNonConformes(String fournisseur, LocalDate date) {
        List<FicheDeRefus> fiches = ficheDeRefusRepository.findFichesDeRefusByFilters(fournisseur, date);

        return fiches.stream().map(f -> {
            BLFicheDeRefusDTO dto = new BLFicheDeRefusDTO();
            dto.setId(f.getId());
            dto.setNumBL(f.getNumBL());
            dto.setReference(f.getReference());
            dto.setFournisseur(f.getFournisseur());
            dto.setVerificateur(f.getVerificateur());
            dto.setDateDeControle(f.getDateDeControle());
            dto.setMotifRefus(f.getMotifRefus());
            dto.setRaison(f.getRaison());
            dto.setQuantiteIncorrecte(f.getQuantiteIncorrecte());
            return dto;
        }).collect(Collectors.toList());
    }
}
