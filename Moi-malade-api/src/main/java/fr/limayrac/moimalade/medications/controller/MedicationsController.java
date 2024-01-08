package fr.limayrac.moimalade.medications.controller;

import fr.limayrac.moimalade.medications.model.Medications;
import fr.limayrac.moimalade.medications.model.MedicationsAssembler;
import fr.limayrac.moimalade.medications.service.MedicationsService;
import fr.limayrac.moimalade.exception.RessourceNotFoundException;
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
public class MedicationsController {

    @Autowired
    private MedicationsService medicationsService;

    private final MedicationsAssembler medicationsAssembler;

    MedicationsController(MedicationsAssembler medicationsAssembler) {
        this.medicationsAssembler = medicationsAssembler;
    }

    @GetMapping(value = "/medications")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des medicaments.",
            tags = { "Medications" })
    public CollectionModel<EntityModel<Medications>> getMedications(@RequestParam(value = "content", required = false) String content,
                                                                    @RequestParam(value = "lang", required = false) String lang) {
        List<EntityModel<Medications>> commonMedications;

        if (content == null && lang == null) {
            commonMedications = medicationsService.getMedications().stream()
                    .map(medicationsAssembler::toModel)
                    .toList();
        } else {
            commonMedications = medicationsService.getMedicationsByNomContaining(content, lang).stream()
                            .map(medicationsAssembler::toModel)
                            .toList();
        }

        return CollectionModel.of(commonMedications, linkTo(methodOn(MedicationsController.class).getMedications(null, null)).withSelfRel());
    }

    @GetMapping(value = "/medications/{idMedication}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'un medicament.",
            tags = { "Medications" })
    public ResponseEntity<?> getMedication(@PathVariable("idMedication") final Integer id) {
        Medications commonMedication = medicationsService.getMedicationById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le medicament %s.".formatted(id)));

        if (commonMedication != null) {
            return ResponseEntity.ok(medicationsAssembler.toModel(commonMedication));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/medications")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer un medicament.",
            tags = { "Medications" })
    public ResponseEntity<?> saveMedication(@RequestParam String nomFr,
                                            @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(medicationsService.saveMedication(Medications.builder()
                .nomFr(nomFr)
                .nomEn(nomEn)
                .build())
        );
    }

    @PutMapping(value = "/medications/{idMedication}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier un medicament.",
            tags = { "Medications" })
    public ResponseEntity<?> putMedication(@PathVariable(name = "idMedication") Integer id,
                                           @RequestParam(required = false) String nomFr,
                                           @RequestParam(required = false) String nomEn) {
        Medications commonMedication = medicationsService.getMedicationById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Medicament introuvable"));

        if (commonMedication != null) {
            if (!nomFr.equals("")) {
                commonMedication.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                commonMedication.setNomEn(nomEn);
            }

            medicationsService.saveMedication(commonMedication);

            return ResponseEntity.ok(commonMedication);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/medications/{idMedication}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer un medicament.",
            tags = { "Medications" })
    public ResponseEntity<?> deleteMedication(@PathVariable(name = "idMedication") Integer id) {
        Medications commonMedication = medicationsService.getMedicationById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Medicament introuvable"));

        if (commonMedication != null) {
            medicationsService.deleteMedication(commonMedication);

            return ResponseEntity.ok("Medicament supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
