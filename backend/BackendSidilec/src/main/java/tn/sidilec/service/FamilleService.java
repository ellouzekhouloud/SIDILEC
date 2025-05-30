package tn.sidilec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.sidilec.Entity.Famille;
import tn.sidilec.Repository.FamilleRepository;

@Service
public class FamilleService {

    @Autowired
    private FamilleRepository familleRepository;

    
    public List<Famille> getAllFamilles() {
        return familleRepository.findAll();
    }

    
    public Famille addFamille(Famille famille) {
        return familleRepository.save(famille);
    }
    
    public void deleteFamille(Long idFamille) {
        familleRepository.deleteById(idFamille);
    }
}
