package tn.sidilec.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.EtiquetteVerte;

@Repository
public interface EtiquetteVerteRepository extends JpaRepository<EtiquetteVerte, Long> {
	 @Query("SELECT e FROM EtiquetteVerte e WHERE " +
	           "(:fournisseur IS NULL OR e.fournisseur = :fournisseur) AND " +
	           "(:date IS NULL OR e.dateDeControle = :date)")
	    List<EtiquetteVerte> findEtiquettesVertesByFilters(@Param("fournisseur") String fournisseur,
	                                                       @Param("date") LocalDate date);
}


