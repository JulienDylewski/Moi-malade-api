package fr.limayrac.moimalade.security.service;

import fr.limayrac.moimalade.security.model.ApiKey;
import fr.limayrac.moimalade.security.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public Optional<ApiKey> getApiKeyById(Integer apiId) {
        return apiKeyRepository.findById(apiId);
    }

    public Optional<ApiKey> getApiKey(final String apiKey) {
        return apiKeyRepository.findByApiKey(apiKey);
    }

    public List<ApiKey> getAllApikey() {
        return apiKeyRepository.findAll();
    }

    public ApiKey saveApiKey(ApiKey apiKey) {
        return apiKeyRepository.save(apiKey);
    }

    public void deleteApiKey(ApiKey apiKey) {
        apiKeyRepository.delete(apiKey);
    }
}
