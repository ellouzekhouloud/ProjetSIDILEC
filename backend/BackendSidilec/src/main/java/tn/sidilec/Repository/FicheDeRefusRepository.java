package tn.sidilec.Repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.FicheDeRefus;

@Repository
public interface FicheDeRefusRepository extends JpaRepository<FicheDeRefus, Long> {
	@Query("SELECT MAX(f.numeroFiche) FROM FicheDeRefus f")
	Long findMaxNumeroFiche();
	
	@Query("SELECT f FROM FicheDeRefus f WHERE " +
	           "(:fournisseur IS NULL OR f.fournisseur = :fournisseur) AND " +
	           "(:date IS NULL OR f.dateDeControle = :date)")
	    List<FicheDeRefus> findFichesDeRefusByFilters(@Param("fournisseur") String fournisseur,
	                                                  @Param("date") LocalDate date);
	
}