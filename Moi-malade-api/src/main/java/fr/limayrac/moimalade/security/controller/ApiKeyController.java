package fr.limayrac.moimalade.security.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.security.model.ApiKey;
import fr.limayrac.moimalade.security.model.ApiKeyAssembler;
import fr.limayrac.moimalade.security.service.ApiKeyService;
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
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    private final ApiKeyAssembler apiKeyAssembler;

    ApiKeyController(ApiKeyAssembler apiKeyAssembler) {
        this.apiKeyAssembler = apiKeyAssembler;
    }

    @GetMapping(value = "/apikey")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des cles api.",
            tags = { "ApiKey" })
    public CollectionModel<EntityModel<ApiKey>> getAllApiKey() {
        List<EntityModel<ApiKey>> apiKeys = apiKeyService.getAllApikey().stream()
                .map(apiKeyAssembler::toModel)
                .toList();

        return CollectionModel.of(apiKeys, linkTo(methodOn(ApiKeyController.class).getAllApiKey()).withSelfRel());
    }

    @GetMapping(value = "/apikey/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'une cle api.",
            tags = { "ApiKey" })
    public ResponseEntity<?> getApiKey(@PathVariable("id") final Integer id) {
        ApiKey apiKey = apiKeyService.getApiKeyById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la cle api %s.".formatted(id)));

        if (apiKey != null) {
            return ResponseEntity.ok(apiKeyAssembler.toModel(apiKey));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/apikey")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer une cle api.",
            tags = { "ApiKey" })
    public ResponseEntity<?> saveApiKey(@RequestParam String apiKey) {
        if (apiKey.equals("")) {
            return ResponseEntity.badRequest().body("Saisir une cle api");
        }

        return ResponseEntity.ok(apiKeyService.saveApiKey(ApiKey.builder()
                .apiKey(apiKey)
                .build())
        );
    }

    @PutMapping(value = "/apikey/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier une cle api.",
            tags = { "ApiKey" })
    public ResponseEntity<?> putApiKey(@PathVariable(name = "id") Integer id,
                                       @RequestParam String apiKey) {
        ApiKey key = apiKeyService.getApiKeyById(id)
                .orElseThrow(() -> new RessourceNotFoundException("ApiKey introuvable"));

        if (key != null) {
            if (!apiKey.equals("")) {
                key.setApiKey(apiKey);
            }

            apiKeyService.saveApiKey(key);

            return ResponseEntity.ok(key);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/apikey/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer une cle api.",
            tags = { "ApiKey" })
    public ResponseEntity<?> deleteApiKey(@PathVariable(name = "id") Integer id) {
        ApiKey apiKey = apiKeyService.getApiKeyById(id)
                .orElseThrow(() -> new RessourceNotFoundException("ApiKey introuvable"));

        if (apiKey != null) {
            apiKeyService.deleteApiKey(apiKey);

            return ResponseEntity.ok("ApiKey supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
