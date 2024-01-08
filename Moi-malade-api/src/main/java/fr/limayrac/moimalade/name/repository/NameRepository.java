package fr.limayrac.moimalade.name.repository;

import fr.limayrac.moimalade.name.model.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Integer> {

}
