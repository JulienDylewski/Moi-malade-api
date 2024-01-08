package fr.limayrac.moimalade.whois.service;

import fr.limayrac.moimalade.whois.model.Whois;
import fr.limayrac.moimalade.whois.repository.WhoisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WhoisService {

    @Autowired
    private WhoisRepository whoisRepository;

    public Optional<Whois> getWhoisById(final Integer id) {
        return whoisRepository.findById(id);
    }

    public List<Whois> getWhoisAll() {
        return whoisRepository.findAll();
    }

    public Whois saveWhois(Whois whois) {
        return whoisRepository.save(whois);
    }

    public void deleteWhois(Whois whois) {
        whoisRepository.delete(whois);
    }
}
