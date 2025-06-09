package tn.sidilec.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.sidilec.Entity.Fournisseur;

import tn.sidilec.service.FournisseurService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@WebMvcTest(FournisseurController.class)
public class FournisseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FournisseurService fournisseurService;

    @MockBean
    private tn.sidilec.service.ProduitService produitService;
    @MockBean
    private tn.sidilec.security.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;
    

    @Test
    public void testAjouterFournisseur() throws Exception {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNomFournisseur("Tunisian Cables");
        fournisseur.setAdresse("Tunis");
        fournisseur.setEmail("contact@tunisian-cables.tn");
        fournisseur.setTelephone("71 000 000");
        fournisseur.setCertificat("ISO 9001");

        // simule la sauvegarde
        Mockito.when(fournisseurService.addFournisseur(any(Fournisseur.class)))
               .thenReturn(fournisseur);

        mockMvc.perform(post("/api/fournisseurs")
        		.with(csrf())
        		.with(user("testUser").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fournisseur)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomFournisseur").value("Tunisian Cables"))
                .andExpect(jsonPath("$.adresse").value("Tunis"))
                .andExpect(jsonPath("$.email").value("contact@tunisian-cables.tn"))
                .andExpect(jsonPath("$.telephone").value("71 000 000"))
                .andExpect(jsonPath("$.certificat").value("ISO 9001"));
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "ADMIN")
    void testDeleteFournisseur() throws Exception {
        Long idFournisseur = 1L;

        // Simuler la suppression dans le service
        Mockito.doNothing().when(fournisseurService).deleteFournisseur(idFournisseur);

        mockMvc.perform(delete("/api/fournisseurs/{id}", idFournisseur)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Vérifie que le service a bien été appelé
        Mockito.verify(fournisseurService).deleteFournisseur(idFournisseur);
    }
}
