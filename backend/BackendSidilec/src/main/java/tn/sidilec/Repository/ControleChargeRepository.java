package tn.sidilec.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.ControleCharge;

@Repository
public interface ControleChargeRepository extends JpaRepository<ControleCharge, Long> {
    List<ControleCharge> findByPersonnelId(Long personnelId);
    List<ControleCharge> findByDebutControleBetween(LocalDateTime debut, LocalDateTime fin);

}
