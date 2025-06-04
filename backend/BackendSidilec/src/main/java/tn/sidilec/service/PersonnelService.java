package tn.sidilec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.sidilec.Entity.Personnel;

import tn.sidilec.Repository.PersonnelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    @Autowired
    private PersonnelRepository personnelRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Personnel> getAllPersonnels() {
        return personnelRepository.findAll();
    }

    public Optional<Personnel> getPersonnelById(Long id) {
        return personnelRepository.findById(id);
    }

    public Optional<Personnel> getPersonnelByMatricule(String matricule) {
        return personnelRepository.findByMatricule(matricule);
    }

    public Personnel addPersonnel(Personnel personnel) {
        if (personnelRepository.findByMatricule(personnel.getMatricule()).isPresent()) {
            throw new RuntimeException("Matricule déjà utilisé !");
        }

        // Hacher le mot de passe
        personnel.setMotDePasse(passwordEncoder.encode(personnel.getMotDePasse()));

        return personnelRepository.save(personnel);
    }

    public Personnel updatePersonnel(Long id, Personnel personnelDetails) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));

        personnel.setNom(personnelDetails.getNom());
        personnel.setPrenom(personnelDetails.getPrenom());
        personnel.setEmail(personnelDetails.getEmail());
        personnel.setMatricule(personnelDetails.getMatricule());
        personnel.setQualifications(personnelDetails.getQualifications());
        //personnel.setRole(personnelDetails.getRole());

        if (personnelDetails.getMotDePasse() != null && !personnelDetails.getMotDePasse().isEmpty()) {
            personnel.setMotDePasse(passwordEncoder.encode(personnelDetails.getMotDePasse()));
        }

        return personnelRepository.save(personnel);
    }

    public void deletePersonnel(Long id) {
        if (!personnelRepository.existsById(id)) {
            throw new RuntimeException("Personnel non trouvé");
        }
        personnelRepository.deleteById(id);
    }
    
    public List<Personnel> getActivePersonnels() {
        return personnelRepository.findByIsActiveTrue();
    }
}
