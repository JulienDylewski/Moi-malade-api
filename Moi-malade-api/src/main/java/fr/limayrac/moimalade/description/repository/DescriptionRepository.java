package fr.limayrac.moimalade.description.repository;

import fr.limayrac.moimalade.description.model.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Integer> {

}
