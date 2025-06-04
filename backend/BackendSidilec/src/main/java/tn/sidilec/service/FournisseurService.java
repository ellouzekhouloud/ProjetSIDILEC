package tn.sidilec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Repository.FournisseurRepository;

@Service
public class FournisseurService {

	@Autowired
    private FournisseurRepository fournisseurRepository;

    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    public Fournisseur addFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur updateFournisseur(Long id, Fournisseur fournisseurDetails) {
        Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
        fournisseur.setNomFournisseur(fournisseurDetails.getNomFournisseur());
        fournisseur.setCertificat(fournisseurDetails.getCertificat());
        fournisseur.setEmail(fournisseurDetails.getEmail());
        fournisseur.setAdresse(fournisseurDetails.getAdresse());
        fournisseur.setTelephone(fournisseurDetails.getTelephone());
        return fournisseurRepository.save(fournisseur);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }
	
}
