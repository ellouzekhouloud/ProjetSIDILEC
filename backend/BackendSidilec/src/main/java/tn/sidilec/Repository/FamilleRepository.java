package tn.sidilec.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.Famille;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, Long> {
  
}