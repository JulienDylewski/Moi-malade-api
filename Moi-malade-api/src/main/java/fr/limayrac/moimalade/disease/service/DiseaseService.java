package fr.limayrac.moimalade.disease.service;

import fr.limayrac.moimalade.disease.model.Disease;
import fr.limayrac.moimalade.disease.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    public Optional<Disease> getDiseaseById(final Integer id) {
        return diseaseRepository.findById(id);
    }

    public List<Disease> getBySexAgeEthnicity(String sex, String ethnicity, Integer age) {
        return diseaseRepository.findBySexAndEthnicity(sex, ethnicity, age);
    }

    public void deleteDisease(Disease disease) {
        diseaseRepository.delete(disease);
    }

    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }
}
