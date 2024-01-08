package fr.limayrac.moimalade.symptoms.repository;

import fr.limayrac.moimalade.symptoms.model.Symptoms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomsRepository extends JpaRepository<Symptoms, Integer> {

}
