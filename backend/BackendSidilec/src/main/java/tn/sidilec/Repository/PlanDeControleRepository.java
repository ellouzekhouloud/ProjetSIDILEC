package tn.sidilec.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.sidilec.Entity.PlanDeControle;

public interface PlanDeControleRepository extends JpaRepository<PlanDeControle, Long> {
	List<PlanDeControle> findByProduit_IdProduit(Long idProduit);
}
