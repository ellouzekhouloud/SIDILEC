package tn.sidilec.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;
import tn.sidilec.Repository.PersonnelRepository;
import tn.sidilec.service.PersonnelService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  
@RequestMapping("/api/personnels")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;
    @Autowired
    private PersonnelRepository personnelRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Personnel> getAllPersonnels() {
        return personnelService.getAllPersonnels();
    }

    @GetMapping("/{id}")
    public Optional<Personnel> getPersonnelById(@PathVariable Long id) {
        return personnelService.getPersonnelById(id);
    }
    @PostMapping
    public Personnel addPersonnel(@RequestBody Personnel personnel) {
       
        if (personnelRepository.findByMatricule(personnel.getMatricule()).isPresent()) {
            throw new RuntimeException("Matricule déjà utilisé !");
        }

        // Vérifier si le rôle est défini dans la requête. Si non, attribuer RESPONSABLE_RECEPTION par défaut.
        if (personnel.getRole() == null) {
            throw new RuntimeException("Le rôle doit être spécifié (RESPONSABLE_RECEPTION ou CONTROLEUR)");
        }

        // Vérifier que le rôle spécifié est valide
        if (!personnel.getRole().equals(Role.RESPONSABLE_RECEPTION) && !personnel.getRole().equals(Role.CONTROLEUR)) {
            throw new RuntimeException("Rôle invalide. Le rôle doit être soit 'RESPONSABLE_RECEPTION' soit 'CONTROLEUR'.");
        }

        // Encoder le mot de passe
        personnel.setMotDePasse(passwordEncoder.encode(personnel.getMotDePasse()));

        
        return personnelRepository.save(personnel);
    }


    @PutMapping("/{id}")
    public Personnel updatePersonnel(@PathVariable Long id, @RequestBody Personnel personnel) {
        return personnelService.updatePersonnel(id, personnel);
    }

    @DeleteMapping("/{id}")
    public void deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
    }
    
    @GetMapping("/personne/{nom}")
    public ResponseEntity<Personnel> getPersonnelByNom(@PathVariable String nom) {
        Personnel personnel = personnelRepository.findByNom(nom)
                    .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));
        return ResponseEntity.ok(personnel);
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivatePersonnel(@PathVariable Long id) {
        Optional<Personnel> personnelOpt = personnelRepository.findById(id);

        if (personnelOpt.isPresent()) {
            Personnel personnel = personnelOpt.get();
            personnel.setActive(false);
            personnelRepository.save(personnel);
            return ResponseEntity.ok("Personnel désactivé avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personnel non trouvé !");
        }
    }
}