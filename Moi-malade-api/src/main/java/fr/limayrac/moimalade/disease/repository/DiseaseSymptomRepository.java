package fr.limayrac.moimalade.disease.repository;

import fr.limayrac.moimalade.disease.model.DiseaseSymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseSymptomRepository extends JpaRepository<DiseaseSymptom, Integer> {

}
