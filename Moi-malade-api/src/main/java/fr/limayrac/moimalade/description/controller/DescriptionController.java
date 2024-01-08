package fr.limayrac.moimalade.description.controller;

import fr.limayrac.moimalade.description.model.Description;
import fr.limayrac.moimalade.description.model.DescriptionAssembler;
import fr.limayrac.moimalade.description.service.DescriptionService;
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
public class DescriptionController {

    @Autowired
    private DescriptionService descriptionService;

    private final DescriptionAssembler descriptionAssembler;

    DescriptionController(DescriptionAssembler descriptionAssembler) {
        this.descriptionAssembler = descriptionAssembler;
    }

    @GetMapping(value = "/descriptions")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des descriptions.",
            tags = { "Description" })
    public CollectionModel<EntityModel<Description>> getDescriptions() {
        List<EntityModel<Description>> descriptions = descriptionService.getDescriptions().stream()
                .map(descriptionAssembler::toModel)
                .toList();

        return CollectionModel.of(descriptions, linkTo(methodOn(DescriptionController.class).getDescriptions()).withSelfRel());
    }

    @GetMapping(value = "/descriptions/{idDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'une description.",
            tags = { "Description" })
    public ResponseEntity<?> getDescription(@PathVariable("idDescription") final Integer id) {
        Description description = descriptionService.getDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la description %s.".formatted(id)));

        if (description != null) {
            return ResponseEntity.ok(descriptionAssembler.toModel(description));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/descriptions")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer une description.",
            tags = { "Description" })
    public ResponseEntity<?> saveDescription(@RequestParam String nomFr,
                                             @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(descriptionService.saveDescription(Description.builder()
                .nomFr(nomFr)
                .nomEn(nomEn)
                .build())
        );
    }

    @PutMapping(value = "/descriptions/{idDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier une description.",
            tags = { "Description" })
    public ResponseEntity<?> putDescription(@PathVariable(name = "idDescription") Integer id,
                                            @RequestParam(required = false) String nomFr,
                                            @RequestParam(required = false) String nomEn) {
        Description description = descriptionService.getDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (description != null) {
            if (!nomFr.equals("")) {
                description.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                description.setNomEn(nomEn);
            }

            descriptionService.saveDescription(description);

            return ResponseEntity.ok(description);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/descriptions/{idDescription}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer une description.",
            tags = { "Description" })
    public ResponseEntity<?> deleteDescription(@PathVariable(name = "idDescription") Integer id) {
        Description description = descriptionService.getDescriptionById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Description introuvable"));

        if (description != null) {
            descriptionService.deleteDescription(description);

            return ResponseEntity.ok("Description supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
