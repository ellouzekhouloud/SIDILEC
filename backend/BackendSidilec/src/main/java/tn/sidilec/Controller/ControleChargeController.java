package tn.sidilec.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.sidilec.Entity.ControleCharge;
import tn.sidilec.service.ControleChargeService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charge")
@CrossOrigin(origins = "http://localhost:4200")  
public class ControleChargeController {

    @Autowired
    private ControleChargeService controleChargeService;

    @PostMapping("/demarrer")
    public ControleCharge demarrerControle(
            @RequestParam Long personnelId,
           
            @RequestParam Long blId) {
        return controleChargeService.demarrerControle(personnelId, blId);
    }

    @PutMapping("/terminer/{id}")
    public ControleCharge terminerControle(@PathVariable Long id) {
        return controleChargeService.terminerControle(id);
    }

    @GetMapping("/personnel/{id}")
    public List<ControleCharge> getChargesByPersonnel(@PathVariable Long id) {
        return controleChargeService.getChargeByPersonnel(id);
    }
    @GetMapping("/personnel/{id}/temps-total")
    public String getTempsTotalTravaille(@PathVariable Long id) {
        return controleChargeService.calculerHeuresTravaillees(id);
    }
    @GetMapping("/charges-par-personnel")
    public Map<String, String> getChargeTotaleParTousLesPersonnels() {
        return controleChargeService.calculerChargeTotaleParTousLesPersonnels();
    }
   
}