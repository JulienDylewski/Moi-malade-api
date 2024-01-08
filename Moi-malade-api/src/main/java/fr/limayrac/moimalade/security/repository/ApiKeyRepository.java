package fr.limayrac.moimalade.security.repository;

import fr.limayrac.moimalade.security.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {

    Optional<ApiKey> findByApiKey(final String apiKey);
}
