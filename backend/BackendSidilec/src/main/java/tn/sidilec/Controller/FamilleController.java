package tn.sidilec.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Entity.Famille;
import tn.sidilec.service.FamilleService;

@RestController
@RequestMapping("/api/familles")
@CrossOrigin(origins = "http://localhost:4200")
public class FamilleController {

    @Autowired
    private FamilleService familleService;

    
    @GetMapping
    public List<Famille> getAllFamilles() {
        return familleService.getAllFamilles();
    }

  
    @PostMapping
    public Famille addFamille(@RequestBody Famille famille) {
        return familleService.addFamille(famille);
    }
    
    @PutMapping("/{id}")
    public Famille updateFamille(@PathVariable Long id, @RequestBody Famille updatedFamille) {
        return familleService.updateFamille(id, updatedFamille);
    }
    
    @DeleteMapping("/{id}")
    public void deleteFamille(@PathVariable Long id) {
        familleService.deleteFamille(id);
    }
}