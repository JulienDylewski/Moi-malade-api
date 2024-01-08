package fr.limayrac.moimalade.testsprocedures.service;

import fr.limayrac.moimalade.testsprocedures.model.TestsProcedures;
import fr.limayrac.moimalade.testsprocedures.repository.TestsProceduresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestsProceduresService {

    @Autowired
    private TestsProceduresRepository testsProceduresRepository;

    public Optional<TestsProcedures> getTestProcedureById(final Integer id) {
        return testsProceduresRepository.findById(id);
    }

    public List<TestsProcedures> getTestsProcedures() {
        return testsProceduresRepository.findAll();
    }

    public TestsProcedures saveTestProcedure(TestsProcedures testsProcedures) {
        return testsProceduresRepository.save(testsProcedures);
    }

    public void deleteTestProcedure(TestsProcedures testsProcedures) {
        testsProceduresRepository.delete(testsProcedures);
    }
}
