package fr.limayrac.moimalade.name.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.name.model.Name;
import fr.limayrac.moimalade.name.model.NameAssembler;
import fr.limayrac.moimalade.name.service.NameService;
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
public class NameController {

    @Autowired
    private NameService nameService;

    private final NameAssembler nameAssembler;

    NameController(NameAssembler nameAssembler) {
        this.nameAssembler = nameAssembler;
    }

    @GetMapping(value = "/names")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des names.",
            tags = { "Name" })
    public CollectionModel<EntityModel<Name>> getNames() {
        List<EntityModel<Name>> names = nameService.getNames().stream()
                .map(nameAssembler::toModel)
                .toList();

        return CollectionModel.of(names, linkTo(methodOn(NameController.class).getNames()).withSelfRel());
    }

    @GetMapping(value = "/names/{idName}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'un name.",
            tags = { "Name" })
    public ResponseEntity<?> getName(@PathVariable("idName") final Integer id) {
        Name name = nameService.getNameById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le name %s.".formatted(id)));

        if (name != null) {
            return ResponseEntity.ok(nameAssembler.toModel(name));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/names")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer un name.",
            tags = { "Name" })
    public ResponseEntity<?> saveName(@RequestParam String nomFr,
                                      @RequestParam String nomEn) {
        if (nomFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (nomEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(nameService.saveName(Name.builder()
                .nomFr(nomFr)
                .nomEn(nomEn)
                .build())
        );
    }

    @PutMapping(value = "/names/{idName}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier un name.",
            tags = { "Name" })
    public ResponseEntity<?> putName(@PathVariable(name = "idName") Integer id,
                                     @RequestParam(required = false) String nomFr,
                                     @RequestParam(required = false) String nomEn) {
        Name name = nameService.getNameById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Name introuvable"));

        if (name != null) {
            if (!nomFr.equals("")) {
                name.setNomFr(nomFr);
            }

            if (!nomEn.equals("")) {
                name.setNomEn(nomEn);
            }

            nameService.saveName(name);

            return ResponseEntity.ok(name);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/names/{idName}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer un name.",
            tags = { "Name" })
    public ResponseEntity<?> deleteName(@PathVariable(name = "idName") Integer id) {
        Name name = nameService.getNameById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Name introuvable"));

        if (name != null) {
            nameService.deleteName(name);

            return ResponseEntity.ok("Name supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
