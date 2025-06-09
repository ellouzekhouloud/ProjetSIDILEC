package tn.sidilec.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;
import tn.sidilec.Repository.PersonnelRepository;
import tn.sidilec.Security.TestSecurityConfig;
import tn.sidilec.security.JwtService;
import tn.sidilec.service.AuthService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PersonnelRepository personnelRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginSuccess() throws Exception {
        // Arrange
        Personnel personnel = new Personnel();
        personnel.setEmail("test@test.com");
        personnel.setMotDePasse("encodedPassword");
        personnel.setRole(Role.ADMIN);
        personnel.setNom("Test");
        personnel.setPrenom("User");
        personnel.setActive(true);
        personnel.setId(1L);

        Mockito.when(authService.authenticate(anyString(), anyString()))
               .thenReturn(Optional.of(personnel));
        Mockito.when(jwtService.generateAccessToken(Mockito.anyString(), Mockito.anyString()))
               .thenReturn("fakeAccessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.anyString()))
               .thenReturn("fakeRefreshToken");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
        		.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new LoginRequest("test@test.com", "Test1234")
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fakeAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("fakeRefreshToken"))
                .andExpect(jsonPath("$.message").value("Connexion réussie"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.nom").value("Test"))
                .andExpect(jsonPath("$.prenom").value("User"))
                .andExpect(jsonPath("$.personnelId").value("1"));
    }

    static class LoginRequest {
        private String email;
        private String motDePasse;

        public LoginRequest(String email, String motDePasse) {
            this.email = email;
            this.motDePasse = motDePasse;
        }

        public String getEmail() {
            return email;
        }

        public String getMotDePasse() {
            return motDePasse;
        }
    }
}
