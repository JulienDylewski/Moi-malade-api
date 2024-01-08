package fr.limayrac.moimalade.description.service;

import fr.limayrac.moimalade.description.model.Description;
import fr.limayrac.moimalade.description.repository.DescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DescriptionService {

    @Autowired
    private DescriptionRepository descriptionRepository;

    public Optional<Description> getDescriptionById(final Integer id) {
        return descriptionRepository.findById(id);
    }

    public List<Description> getDescriptions() {
        return descriptionRepository.findAll();
    }

    public Description saveDescription(Description description) {
        return descriptionRepository.save(description);
    }

    public void deleteDescription(Description description) {
        descriptionRepository.delete(description);
    }
}
