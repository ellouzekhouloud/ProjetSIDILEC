package tn.sidilec.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.BL;
import tn.sidilec.Entity.BLProduit;

@Repository
public interface BLRepository extends JpaRepository<BL, Long> {
	
	Optional<BL> findByProduits_Produit_Reference(String referenceProduit);
	List<BL> findAllByProduits_Produit_ReferenceOrderByDateReceptionDesc(String referenceProduit);
	
	@Query("SELECT b FROM BL b JOIN b.produits bp WHERE bp.produit.reference = :referenceProduit ORDER BY b.dateReception DESC")
	Optional<BL> findLatestBLWithProductReference(@Param("referenceProduit") String referenceProduit);
	
	@Query("SELECT bp FROM BLProduit bp WHERE bp.produit.id = :produitId AND bp.status = :status")
	List<BLProduit> findByProduitIdAndStatus(@Param("produitId") Long produitId, @Param("status") String status);


	 List<BL> findByTermineFalse();
	 
	 boolean existsByNumBL(String numBL);
	
}
