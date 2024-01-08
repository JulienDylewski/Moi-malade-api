package fr.limayrac.moimalade.whois.repository;

import fr.limayrac.moimalade.whois.model.Whois;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhoisRepository extends JpaRepository<Whois, Integer> {

}
