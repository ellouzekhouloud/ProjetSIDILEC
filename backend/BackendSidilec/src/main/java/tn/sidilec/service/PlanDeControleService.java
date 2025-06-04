package tn.sidilec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.PlanDeControle;
import tn.sidilec.Repository.PlanDeControleRepository;

@Service
public class PlanDeControleService {

    @Autowired
    private PlanDeControleRepository planDeControleRepository;

    
    public List<PlanDeControle> ajouterPlansDeControle(List<PlanDeControle> plans) {
        return planDeControleRepository.saveAll(plans);
    }
    public PlanDeControle ajouterPlanDeControle(PlanDeControle plan) {
        return planDeControleRepository.save(plan);
    }

    public List<PlanDeControle> getPlansByProduit(Long produitId) {
        return planDeControleRepository.findByProduit_IdProduit(produitId);
    }

    public PlanDeControle modifierPlanDeControle(Long id, PlanDeControle planDetails) {
        return planDeControleRepository.findById(id).map(plan -> {
            plan.setCaracteristique(planDetails.getCaracteristique());
            plan.setDonneeTechnique(planDetails.getDonneeTechnique());
            plan.setTolerance(planDetails.getTolerance());
            plan.setFrequenceEtTailleDePrelevement(planDetails.getFrequenceEtTailleDePrelevement());
            plan.setMoyenDeControle(planDetails.getMoyenDeControle());
            plan.setMethodeDeControle(planDetails.getMethodeDeControle());
            return planDeControleRepository.save(plan);
        }).orElseThrow(() -> new RuntimeException("Plan de contrôle non trouvé avec l'id: " + id));
    }

    public void supprimerPlanDeControle(Long id) {
        if (!planDeControleRepository.existsById(id)) {
            throw new RuntimeException("Plan de contrôle non trouvé avec l'id: " + id);
        }
        planDeControleRepository.deleteById(id);
    }
}
