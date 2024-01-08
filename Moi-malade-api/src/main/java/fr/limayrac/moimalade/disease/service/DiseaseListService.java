package fr.limayrac.moimalade.disease.service;

import fr.limayrac.moimalade.disease.model.DiseaseList;
import fr.limayrac.moimalade.disease.repository.DiseaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseListService {

    @Autowired
    private DiseaseListRepository diseaseListRepository;

    public List<DiseaseList> getAll() {
        return diseaseListRepository.findAll();
    }
}
