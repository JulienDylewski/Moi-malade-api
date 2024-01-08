package fr.limayrac.moimalade.disease.repository;

import fr.limayrac.moimalade.disease.model.DiseaseList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseListRepository extends JpaRepository<DiseaseList, Integer> {

}
