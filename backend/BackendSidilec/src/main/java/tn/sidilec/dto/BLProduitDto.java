package tn.sidilec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BLProduitDto {
	private Long blId;
    private String numBL;
    private String dateReception;
    private int quantite;
    private String status;
    private Long idProduit;
    
    
}
