package fr.limayrac.moimalade.name.service;

import fr.limayrac.moimalade.name.model.Name;
import fr.limayrac.moimalade.name.repository.NameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NameService {

    @Autowired
    private NameRepository nameRepository;

    public Optional<Name> getNameById(final Integer id) {
        return nameRepository.findById(id);
    }

    public List<Name> getNames() {
        return nameRepository.findAll();
    }

    public Name saveName(Name name) {
        return nameRepository.save(name);
    }

    public void deleteName(Name name) {
        nameRepository.delete(name);
    }
}
