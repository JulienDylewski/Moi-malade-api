package fr.limayrac.moimalade.symptoms.service;

import fr.limayrac.moimalade.symptoms.model.Symptoms;
import fr.limayrac.moimalade.symptoms.repository.SymptomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SymptomsService {

    @Autowired
    private SymptomsRepository symptomsRepository;

    public Optional<Symptoms> getSymptomById(final Integer id) {
        return symptomsRepository.findById(id);
    }

    public List<Symptoms> getSymptoms() {
        return symptomsRepository.findAll();
    }

    public Symptoms saveSymptom(Symptoms symptom) {
        return symptomsRepository.save(symptom);
    }

    public void deleteSymptom(Symptoms symptom) {
        symptomsRepository.delete(symptom);
    }
}
