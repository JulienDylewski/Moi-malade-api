package fr.limayrac.moimalade.medicationsdescription.repository;

import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationDescriptionRepository extends JpaRepository<MedicationDescription, Integer> {

}
