package tn.sidilec.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.sidilec.Entity.Fournisseur;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
	
	Optional<Fournisseur> findByNomFournisseur(String nomFournisseur);
	
}
