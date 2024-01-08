package fr.limayrac.moimalade.medications.service;

import fr.limayrac.moimalade.medications.model.Medications;
import fr.limayrac.moimalade.medications.repository.MedicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationsService {

    @Autowired
    private MedicationsRepository medicationsRepository;

    public Optional<Medications> getMedicationById(final Integer id) {
        return medicationsRepository.findById(id);
    }

    public List<Medications> getMedications() {
        return medicationsRepository.findAll();
    }

    public Medications saveMedication(Medications commonMedication) {
        return medicationsRepository.save(commonMedication);
    }

    public void deleteMedication(Medications commonMedication) {
        medicationsRepository.delete(commonMedication);
    }

    public List<Medications> getMedicationsByNomContaining(String content, String lang) {
        List<Medications> list;

        switch (lang) {
            case "en":
                list = medicationsRepository.findByNomEnContainingOrderByNomEnDesc(content);
                break;
            case "fr":
                list = medicationsRepository.findByNomFrContainingOrderByNomFrDesc(content);
                break;
            default:
                return Collections.emptyList();
        }

        return list;
    }
}
