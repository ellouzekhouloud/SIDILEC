package tn.sidilec.Repository;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.sidilec.Entity.Fournisseur;
import tn.sidilec.Entity.Produit;
import tn.sidilec.dto.QuantiteFournisseurDTO;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByFournisseur_IdFournisseur(Long fournisseurId);
    
    Optional<Produit> findByReference(String reference);
    
    long countByFournisseur(Fournisseur fournisseur);
    
 // Produits non conformes, nombre de BL , PPm par fournisseurs
    // les produits non conformes
    @Query("""
    	    SELECT f.fournisseur AS fournisseur, COUNT(f) AS nonConformes
    	    FROM FicheDeRefus f
    	    WHERE f.dateDeControle BETWEEN :startDate AND :endDate
    	    GROUP BY f.fournisseur
    	    ORDER BY COUNT(f) DESC
    	""")
    	List<Map<String, Object>> countNonConformesByFournisseur(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);
    // nombre de BL
    @Query("""
    	    SELECT c.fournisseur AS fournisseur,
    	           COUNT(DISTINCT c.numBL) AS nombreBL
    	    FROM Controle c
    	    WHERE c.dateDeControle BETWEEN :startDate AND :endDate
    	    GROUP BY c.fournisseur
    	""")
    	List<Map<String, Object>> countBLByFournisseurFromControle(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);

    
    
    // quantité receptionnée par fournisseur + quantité non conforme 
    
    @Query("SELECT new tn.sidilec.dto.QuantiteFournisseurDTO(f.nomFournisseur, " +
            "SUM(DISTINCT bp.quantite), " +
            "SUM(CASE WHEN c.quantiteIncorrecte IS NOT NULL THEN c.quantiteIncorrecte ELSE 0 END)) " +
            "FROM BLProduit bp " +
            "JOIN bp.bl b " +  
            "JOIN bp.produit p " +
            "JOIN p.fournisseur f " +
            "LEFT JOIN Controle c ON c.produit.idProduit = p.idProduit " +
            "WHERE FUNCTION('STR_TO_DATE', c.dateDeControle, '%Y-%m-%d') BETWEEN :startDate AND :endDate " +
            "GROUP BY f.nomFournisseur")

     List<QuantiteFournisseurDTO> findQuantitesParFournisseur(
         @Param("startDate") LocalDate startDate,
         @Param("endDate") LocalDate endDate
     );

   
}