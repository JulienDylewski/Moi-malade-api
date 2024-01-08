package fr.limayrac.moimalade.testsprocedures.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.testsprocedures.model.TestsProcedures;
import fr.limayrac.moimalade.testsprocedures.model.TestsProceduresAssembler;
import fr.limayrac.moimalade.testsprocedures.service.TestsProceduresService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api")
public class TestsProceduresController {

    @Autowired
    private TestsProceduresService testsProceduresService;

    private final TestsProceduresAssembler testsProceduresAssembler;

    TestsProceduresController(TestsProceduresAssembler testsProceduresAssembler) {
        this.testsProceduresAssembler = testsProceduresAssembler;
    }

    @GetMapping(value = "/testsprocedures")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des tests et procedures.",
            tags = { "TestsProcedures" })
    public CollectionModel<EntityModel<TestsProcedures>> getTestsProcedures() {
        List<EntityModel<TestsProcedures>> testsProcedures = testsProceduresService.getTestsProcedures().stream()
                .map(testsProceduresAssembler::toModel)
                .toList();

        return CollectionModel.of(testsProcedures, linkTo(methodOn(TestsProceduresController.class).getTestsProcedures()).withSelfRel());
    }

    @GetMapping(value = "/testsprocedures/{idTestProcedure}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'un test et procedure.",
            tags = { "TestsProcedures" })
    public ResponseEntity<?> getTestProcedure(@PathVariable("idTestProcedure") final Integer id) {
        TestsProcedures testProcedure = testsProceduresService.getTestProcedureById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le test et procedure %s.".formatted(id)));

        if (testProcedure != null) {
            return ResponseEntity.ok(testsProceduresAssembler.toModel(testProcedure));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/testsprocedures")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer un test et procedure.",
            tags = { "TestsProcedures" })
    public ResponseEntity<?> saveTestProcedure(@RequestParam String nomFr,
                                               @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(testsProceduresService.saveTestProcedure(TestsProcedures.builder()
                .nomFr(nomFr)
                .nomEn(nomEn)
                .build())
        );
    }

    @PutMapping(value = "/testsprocedures/{idTestProcedure}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier un test et procedure.",
            tags = { "TestsProcedures" })
    public ResponseEntity<?> putTestProcedure(@PathVariable(name = "idTestProcedure") Integer id,
                                              @RequestParam(required = false) String nomFr,
                                              @RequestParam(required = false) String nomEn) {
        TestsProcedures testProcedure = testsProceduresService.getTestProcedureById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Test et procedure introuvable"));

        if (testProcedure != null) {
            if (!nomFr.equals("")) {
                testProcedure.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                testProcedure.setNomEn(nomEn);
            }

            testsProceduresService.saveTestProcedure(testProcedure);

            return ResponseEntity.ok(testProcedure);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/testsprocedures/{idTestProcedure}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer un test et procedure.",
            tags = { "TestsProcedures" })
    public ResponseEntity<?> deleteTestProcedure(@PathVariable(name = "idTestProcedure") Integer id) {
        TestsProcedures testProcedure = testsProceduresService.getTestProcedureById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Test et procedure introuvable"));

        if (testProcedure != null) {
            testsProceduresService.deleteTestProcedure(testProcedure);

            return ResponseEntity.ok("Test et procedure supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}