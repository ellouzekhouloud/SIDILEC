package tn.sidilec.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;

import tn.sidilec.Entity.ControleCharge;

import tn.sidilec.Repository.BLRepository;
import tn.sidilec.Repository.ControleChargeRepository;
import tn.sidilec.Repository.PersonnelRepository;


@Service
public class ControleChargeService {

    @Autowired
    private ControleChargeRepository controleChargeRepository;

   

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private BLRepository blRepository;

    public ControleCharge demarrerControle(Long personnelId,  Long blId) {
        ControleCharge charge = new ControleCharge();
        charge.setDebutControle(LocalDateTime.now());
        charge.setPersonnel(personnelRepository.findById(personnelId).orElseThrow());
        
        charge.setBl(blRepository.findById(blId).orElseThrow());
        return controleChargeRepository.save(charge);
    }

    public ControleCharge terminerControle(Long chargeId) {
        ControleCharge charge = controleChargeRepository.findById(chargeId).orElseThrow();
        charge.setFinControle(LocalDateTime.now());
        return controleChargeRepository.save(charge);
    }

    public List<ControleCharge> getChargeByPersonnel(Long personnelId) {
        return controleChargeRepository.findByPersonnelId(personnelId);
    }
    
    public String calculerHeuresTravaillees(Long personnelId) {
        List<ControleCharge> charges = controleChargeRepository.findByPersonnelId(personnelId);

        Duration total = Duration.ZERO;

        for (ControleCharge charge : charges) {
            if (charge.getDebutControle() != null && charge.getFinControle() != null) {
                Duration duree = Duration.between(charge.getDebutControle(), charge.getFinControle());
                total = total.plus(duree);
            }
        }

        long heures = total.toHours();
        long minutes = total.minusHours(heures).toMinutes();

        return heures + "h " + minutes + "min";
    }
    
    public Map<String, String> calculerChargeTotaleParTousLesPersonnels() {
        List<ControleCharge> charges = controleChargeRepository.findAll();

        Map<String, Duration> totalParPersonnel = new HashMap<>();

        for (ControleCharge charge : charges) {
            if (charge.getDebutControle() != null && charge.getFinControle() != null) {
                Duration duree = Duration.between(charge.getDebutControle(), charge.getFinControle());
                String nom = charge.getPersonnel().getNom(); // ou prénom+nom selon ton modèle

                totalParPersonnel.put(
                    nom,
                    totalParPersonnel.getOrDefault(nom, Duration.ZERO).plus(duree)
                );
            }
        }

        // Formatage : "Xh Ymin"
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Duration> entry : totalParPersonnel.entrySet()) {
            long heures = entry.getValue().toHours();
            long minutes = entry.getValue().minusHours(heures).toMinutes();
            result.put(entry.getKey(), heures + "h " + minutes + "min");
        }

        return result;
    }
}