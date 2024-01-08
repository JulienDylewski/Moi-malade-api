package fr.limayrac.moimalade.symptomsdescription.service;

import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescription;
import fr.limayrac.moimalade.symptomsdescription.repository.SymptomDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SymptomDescriptionService {

    @Autowired
    private SymptomDescriptionRepository symptomDescriptionRepository;

    public Optional<SymptomDescription> getSymptomDescriptionById(final Integer id) {
        return symptomDescriptionRepository.findById(id);
    }

    public List<SymptomDescription> getSymptomDescriptions() {
        return symptomDescriptionRepository.findAll();
    }

    public SymptomDescription saveSymptomDescription(SymptomDescription symptomDescription) {
        return symptomDescriptionRepository.save(symptomDescription);
    }

    public void deleteSymptomDescription(SymptomDescription symptomDescription) {
        symptomDescriptionRepository.delete(symptomDescription);
    }
}
