package tn.sidilec.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    @JsonProperty("isRead")
    private boolean isRead = false;

    private String role; // Exemple : "CONTROLEUR"
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @ManyToOne
    @JoinColumn(name = "bl_id")
    private BL bl;
}