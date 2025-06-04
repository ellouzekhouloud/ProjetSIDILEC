package tn.sidilec.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Entity.EtiquetteVerte;
import tn.sidilec.Repository.EtiquetteVerteRepository;
import java.util.List;

@RestController
@RequestMapping("/api/etiquettes")
@CrossOrigin(origins = "http://localhost:4200")  
public class EtiquetteVerteController {

    @Autowired
    private EtiquetteVerteRepository etiquetteVerteRepository;

    @PostMapping
    public EtiquetteVerte saveEtiquette(@RequestBody EtiquetteVerte etiquette) {
        return etiquetteVerteRepository.save(etiquette);
    }

    @GetMapping
    public List<EtiquetteVerte> getAllEtiquettes() {
        return etiquetteVerteRepository.findAll();
    }

}
