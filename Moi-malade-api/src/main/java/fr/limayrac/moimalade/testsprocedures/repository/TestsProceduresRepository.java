package fr.limayrac.moimalade.testsprocedures.repository;

import fr.limayrac.moimalade.testsprocedures.model.TestsProcedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestsProceduresRepository extends JpaRepository<TestsProcedures, Integer> {

}
