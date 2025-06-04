package tn.sidilec.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
	Optional<Personnel> findByMatricule(String matricule);
	Optional<Personnel> findByEmail(String email);
	Optional<Personnel> findByNom(String nom);
	List<Personnel> findByIsActiveTrue();
	List<Personnel> findByRole(Role role);
}
