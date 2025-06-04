package tn.sidilec.Repository;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Entity.Produit;
import tn.sidilec.dto.QuantiteFournisseurDTO;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByFournisseur_IdFournisseur(Long fournisseurId);
    
    Optional<Produit> findByReference(String reference);
    
    long countByFournisseur(Fournisseur fournisseur);
    
 // Produits non conformes, nombre de BL , PPm par fournisseurs
    // les produits non conformes
    @Query("""
    	    SELECT f.fournisseur AS fournisseur, COUNT(f) AS nonConformes
    	    FROM FicheDeRefus f
    	    WHERE f.dateDeControle BETWEEN :startDate AND :endDate
    	    GROUP BY f.fournisseur
    	    ORDER BY COUNT(f) DESC
    	""")
    	List<Map<String, Object>> countNonConformesByFournisseur(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);
    // nombre de BL
    @Query("""
    	    SELECT c.fournisseur AS fournisseur,
    	           COUNT(DISTINCT c.numBL) AS nombreBL
    	    FROM Controle c
    	    WHERE c.dateDeControle BETWEEN :startDate AND :endDate
    	    GROUP BY c.fournisseur
    	""")
    	List<Map<String, Object>> countBLByFournisseurFromControle(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);

    
    
    // quantité receptionnée par fournisseur + quantité non conforme 
    @Query("SELECT new tn.sidilec.dto.QuantiteFournisseurDTO(f.nomFournisseur, " +
    	       "(SELECT SUM(bp2.quantite) FROM BLProduit bp2 JOIN bp2.produit p2 WHERE p2.fournisseur = f AND bp2.bl.dateReception BETWEEN :startDate AND :endDate), " +
    	       "(SELECT SUM(COALESCE(fr.quantiteIncorrecte, 0)) FROM FicheDeRefus fr WHERE fr.numBL IN " +
    	       "(SELECT b2.numBL FROM BL b2 WHERE b2.fournisseur = f AND b2.dateReception BETWEEN :startDate AND :endDate)) " +
    	       ") " +
    	       "FROM Fournisseur f " +
    	       "WHERE EXISTS (SELECT 1 FROM BLProduit bp3 JOIN bp3.produit p3 WHERE p3.fournisseur = f AND bp3.bl.dateReception BETWEEN :startDate AND :endDate) " +
    	       "OR EXISTS (SELECT 1 FROM FicheDeRefus fr2 WHERE fr2.numBL IN (SELECT b3.numBL FROM BL b3 WHERE b3.fournisseur = f AND b3.dateReception BETWEEN :startDate AND :endDate))")
    	List<QuantiteFournisseurDTO> findQuantitesParFournisseur(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);

}