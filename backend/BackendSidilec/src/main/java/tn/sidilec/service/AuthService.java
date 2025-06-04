package tn.sidilec.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.Personnel;
import tn.sidilec.Repository.PersonnelRepository;

@Service
public class AuthService {
    private final PersonnelRepository personnelRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(PersonnelRepository personnelRepository, PasswordEncoder passwordEncoder) {
        this.personnelRepository = personnelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Personnel> authenticate(String email, String motDePasse) {
        Optional<Personnel> personnel = personnelRepository.findByEmail(email);

        if (personnel.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé avec cet email !");
            return Optional.empty();
        }

        if (passwordEncoder.matches(motDePasse, personnel.get().getMotDePasse())) {
            return personnel;
        } else {
            System.out.println("Mot de passe incorrect !");
            return Optional.empty();
        }
    }
}
