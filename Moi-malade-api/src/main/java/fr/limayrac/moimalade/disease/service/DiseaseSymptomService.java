package fr.limayrac.moimalade.disease.service;

import fr.limayrac.moimalade.disease.model.DiseaseSymptom;
import fr.limayrac.moimalade.disease.repository.DiseaseSymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiseaseSymptomService {

    @Autowired
    private DiseaseSymptomRepository diseaseSymptomRepository;

    public DiseaseSymptom saveDiseaseSymptom(DiseaseSymptom diseaseSymptom) {
        return diseaseSymptomRepository.save(diseaseSymptom);
    }

    public void deleteDiseaseSymptom(DiseaseSymptom diseaseSymptom) {
        diseaseSymptomRepository.delete(diseaseSymptom);
    }
}
