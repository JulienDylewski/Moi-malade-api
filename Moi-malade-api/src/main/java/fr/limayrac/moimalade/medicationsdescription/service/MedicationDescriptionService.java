package fr.limayrac.moimalade.medicationsdescription.service;

import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescription;
import fr.limayrac.moimalade.medicationsdescription.repository.MedicationDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationDescriptionService {

    @Autowired
    private MedicationDescriptionRepository medicationDescriptionRepository;

    public Optional<MedicationDescription> getMedicationDescriptionById(final Integer id) {
        return medicationDescriptionRepository.findById(id);
    }

    public List<MedicationDescription> getMedicationsDescription() {
        return medicationDescriptionRepository.findAll();
    }

    public MedicationDescription saveMedicationDescription(MedicationDescription medicationDescription) {
        return medicationDescriptionRepository.save(medicationDescription);
    }

    public void deleteMedicationDescription(MedicationDescription medicationDescription) {
        medicationDescriptionRepository.delete(medicationDescription);
    }
}
