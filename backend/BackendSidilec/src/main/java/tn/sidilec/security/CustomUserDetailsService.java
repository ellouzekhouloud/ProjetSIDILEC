package tn.sidilec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.Personnel;
import tn.sidilec.Repository.PersonnelRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonnelRepository personnelRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Personnel appUser = personnelRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retourner un objet UserDetails valide
        return User.builder()
        		.username(appUser.getEmail())
                .password(appUser.getMotDePasse()) 
                .roles(appUser.getRole().name())
                .build();
    }
}