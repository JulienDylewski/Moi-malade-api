package fr.limayrac.moimalade.symptoms.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.symptoms.model.Symptoms;
import fr.limayrac.moimalade.symptoms.model.SymptomsAssembler;
import fr.limayrac.moimalade.symptoms.service.SymptomsService;
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
public class SymptomsController {

    @Autowired
    private SymptomsService symptomsService;

    private final SymptomsAssembler symptomsAssembler;

    SymptomsController(SymptomsAssembler symptomsAssembler) {
        this.symptomsAssembler = symptomsAssembler;
    }

    @GetMapping(value = "/symptoms")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des symptomes.",
            tags = { "Symptoms" })
    public CollectionModel<EntityModel<Symptoms>> getSymptoms() {
        List<EntityModel<Symptoms>> symptoms = symptomsService.getSymptoms().stream()
                .map(symptomsAssembler::toModel)
                .toList();

        return CollectionModel.of(symptoms, linkTo(methodOn(SymptomsController.class).getSymptoms()).withSelfRel());
    }

    @GetMapping(value = "/symptoms/{idSymptom}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'un symptome.",
            tags = { "Symptoms" })
    public ResponseEntity<?> getSymptom(@PathVariable("idSymptom") final Integer id) {
        Symptoms symptom = symptomsService.getSymptomById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le symptome %s.".formatted(id)));

        if (symptom != null) {
            return ResponseEntity.ok(symptomsAssembler.toModel(symptom));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/symptoms")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer un symptome.",
            tags = { "Symptoms" })
    public ResponseEntity<?> saveSymptom(@RequestParam String nomFr,
                                         @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(symptomsService.saveSymptom(Symptoms.builder()
                                .nomFr(nomFr)
                                .nomEn(nomEn)
                                .build())
        );
    }

    @PutMapping(value = "/symptoms/{idSymptom}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier un symptome.",
            tags = { "Symptoms" })
    public ResponseEntity<?> putSymptom(@PathVariable(name = "idSymptom") Integer id,
                                        @RequestParam(required = false) String nomFr,
                                        @RequestParam(required = false) String nomEn) {
        Symptoms symptom = symptomsService.getSymptomById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Symptome introuvable"));

        if (symptom != null) {
            if (!nomFr.equals("")) {
                symptom.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                symptom.setNomEn(nomEn);
            }

            symptomsService.saveSymptom(symptom);

            return ResponseEntity.ok(symptom);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/symptoms/{idSymptom}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer un symptome.",
            tags = { "Symptoms" })
    public ResponseEntity<?> deleteSymptom(@PathVariable(name = "idSymptom") Integer id) {
        Symptoms symptom = symptomsService.getSymptomById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Symptome introuvable"));

        if (symptom != null) {
            symptomsService.deleteSymptom(symptom);

            return ResponseEntity.ok("Symptome supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
