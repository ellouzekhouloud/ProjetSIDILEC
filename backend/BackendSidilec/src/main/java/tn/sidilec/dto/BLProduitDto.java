package tn.sidilec.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BLProduitDto {
	private Long blId;
    private String numBL;
    private LocalDate dateReception;
    private int quantite;
    private String status;
    private Long idProduit;
    
    
}
