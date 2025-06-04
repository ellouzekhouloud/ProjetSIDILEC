package tn.sidilec.Repository;




import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


import tn.sidilec.Entity.Controle;

@Repository
public interface ControleRepository extends JpaRepository<Controle, Long> {
	Controle findByProduit_IdProduitAndNumBL(Long produitId, String numBL);
	
	
}