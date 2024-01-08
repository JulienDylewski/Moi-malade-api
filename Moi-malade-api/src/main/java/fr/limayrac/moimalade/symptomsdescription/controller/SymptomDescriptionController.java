package fr.limayrac.moimalade.symptomsdescription.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescription;
import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescriptionAssembler;
import fr.limayrac.moimalade.symptomsdescription.service.SymptomDescriptionService;
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
public class SymptomDescriptionController {

    @Autowired
    private SymptomDescriptionService symptomDescriptionService;

    private final SymptomDescriptionAssembler symptomDescriptionAssembler;

    SymptomDescriptionController(SymptomDescriptionAssembler symptomDescriptionAssembler) {
        this.symptomDescriptionAssembler = symptomDescriptionAssembler;
    }

    @GetMapping(value = "/symptomdescriptions")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des symptom descriptions.",
            tags = { "SymptomDescription" })
    public CollectionModel<EntityModel<SymptomDescription>> getSymptomDescriptions() {
        List<EntityModel<SymptomDescription>> symptomDescriptions = symptomDescriptionService.getSymptomDescriptions().stream()
                .map(symptomDescriptionAssembler::toModel)
                .toList();

        return CollectionModel.of(symptomDescriptions, linkTo(methodOn(SymptomDescriptionController.class).getSymptomDescriptions()).withSelfRel());
    }

    @GetMapping(value = "/symptomdescriptions/{idSymptomDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'une symptom description.",
            tags = { "SymptomDescription" })
    public ResponseEntity<?> getSymptomDescription(@PathVariable("idSymptomDescription") final Integer id) {
        SymptomDescription symptomDescription = symptomDescriptionService.getSymptomDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la description %s.".formatted(id)));

        if (symptomDescription != null) {
            return ResponseEntity.ok(symptomDescriptionAssembler.toModel(symptomDescription));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/symptomdescriptions")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer une symtom description.",
            tags = { "SymptomDescription" })
    public ResponseEntity<?> saveSymptomDescription(@RequestParam String nomFr,
                                                    @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(symptomDescriptionService.saveSymptomDescription(SymptomDescription.builder()
                .nomFr(nomFr)
                .nomEn(nomEn)
                .build())
        );
    }

    @PutMapping(value = "/symptomdescriptions/{idSymptomDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier une symptom description.",
            tags = { "SymptomDescription" })
    public ResponseEntity<?> putDescription(@PathVariable(name = "idSymptomDescription") Integer id,
                                            @RequestParam(required = false) String nomFr,
                                            @RequestParam(required = false) String nomEn) {
        SymptomDescription symptomDescription = symptomDescriptionService.getSymptomDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (symptomDescription != null) {
            if (!nomFr.equals("")) {
                symptomDescription.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                symptomDescription.setNomEn(nomEn);
            }

            symptomDescriptionService.saveSymptomDescription(symptomDescription);

            return ResponseEntity.ok(symptomDescription);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/symptomdescriptions/{idSymptomDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer une symptom description.",
            tags = { "Description" })
    public ResponseEntity<?> deleteSymptomDescription(@PathVariable(name = "idSymptomDescription") Integer id) {
        SymptomDescription symptomDescription = symptomDescriptionService.getSymptomDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (symptomDescription != null) {
            symptomDescriptionService.deleteSymptomDescription(symptomDescription);

            return ResponseEntity.ok("Description supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
