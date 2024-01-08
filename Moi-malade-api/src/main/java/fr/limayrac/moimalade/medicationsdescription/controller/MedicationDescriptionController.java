package fr.limayrac.moimalade.medicationsdescription.controller;

import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescription;
import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescriptionAssembler;
import fr.limayrac.moimalade.medicationsdescription.service.MedicationDescriptionService;
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
public class MedicationDescriptionController {

    @Autowired
    private MedicationDescriptionService medicationDescriptionService;

    private final MedicationDescriptionAssembler medicationDescriptionAssembler;

    MedicationDescriptionController(MedicationDescriptionAssembler medicationDescriptionAssembler) {
        this.medicationDescriptionAssembler = medicationDescriptionAssembler;
    }

    @GetMapping(value = "/medicationdescription")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des descriptions de medicaments.",
            tags = { "MedicationDescription" })
    public CollectionModel<EntityModel<MedicationDescription>> getMedicationsDescription() {
        List<EntityModel<MedicationDescription>> commonMedicationsDescription = medicationDescriptionService
                .getMedicationsDescription().stream()
                .map(medicationDescriptionAssembler::toModel)
                .toList();

        return CollectionModel.of(commonMedicationsDescription, linkTo(methodOn(MedicationDescriptionController.class).getMedicationsDescription()).withSelfRel());
    }

    @GetMapping(value = "/medicationdescription/{idMedicationDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'une description de medicament.",
            tags = { "MedicationDescription" })
    public ResponseEntity<?> getMedicationDescription(@PathVariable("idMedicationDescription") final Integer id) {
        MedicationDescription medicationDescription = medicationDescriptionService.getMedicationDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la description %s.".formatted(id)));

        if (medicationDescription != null) {
            return ResponseEntity.ok(medicationDescriptionAssembler.toModel(medicationDescription));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/medicationdescription")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer une description.",
            tags = { "MedicationDescription" })
    public ResponseEntity<?> saveMedicationDescription(@RequestParam String descriptionFr,
                                                       @RequestParam String descriptionEn) {
        if (descriptionFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir une description en français");
        }

        if (descriptionEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir une description en anglais");
        }

        return ResponseEntity.ok(medicationDescriptionService.saveMedicationDescription(MedicationDescription.builder()
                .descriptionFr(descriptionFr)
                .descriptionEn(descriptionEn)
                .build())
        );
    }

    @PutMapping(value = "/medicationdescription/{idMedicationDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier une description.",
            tags = { "MedicationDescription" })
    public ResponseEntity<?> putMedicationDescription(@PathVariable(name = "idMedicationDescription") Integer id,
                                                      @RequestParam(required = false) String descriptionFr,
                                                      @RequestParam(required = false) String descriptionEn) {
        MedicationDescription medicationDescription = medicationDescriptionService.getMedicationDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (medicationDescription != null) {
            if (!descriptionFr.equals("")) {
                medicationDescription.setDescriptionFr(descriptionFr);
            }

            if (!descriptionEn.equals("")) {
                medicationDescription.setDescriptionEn(descriptionEn);
            }

            medicationDescriptionService.saveMedicationDescription(medicationDescription);

            return ResponseEntity.ok(medicationDescription);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/medicationdescription/{idMedicationDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer une description.",
            tags = { "MedicationDescription" })
    public ResponseEntity<?> deleteMedicationDescription(@PathVariable(name = "idMedicationDescription") Integer id) {
        MedicationDescription medicationDescription = medicationDescriptionService.getMedicationDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (medicationDescription != null) {
            medicationDescriptionService.deleteMedicationDescription(medicationDescription);

            return ResponseEntity.ok("Description supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
