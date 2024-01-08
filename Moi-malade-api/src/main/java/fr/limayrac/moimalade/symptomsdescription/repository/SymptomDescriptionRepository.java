package fr.limayrac.moimalade.symptomsdescription.repository;

import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomDescriptionRepository extends JpaRepository<SymptomDescription, Integer> {

}
