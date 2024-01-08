package fr.limayrac.moimalade.medications.repository;

import fr.limayrac.moimalade.medications.model.Medications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationsRepository extends JpaRepository<Medications, Integer> {

    List<Medications> findByNomFrContainingOrderByNomFrDesc(String content);

    List<Medications> findByNomEnContainingOrderByNomEnDesc(String content);
}
