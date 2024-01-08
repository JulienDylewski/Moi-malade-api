package fr.limayrac.moimalade.whois.controller;

import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.whois.model.Whois;
import fr.limayrac.moimalade.whois.model.WhoisAssembler;
import fr.limayrac.moimalade.whois.service.WhoisService;
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
public class WhoisController {

    @Autowired
    private WhoisService whoisService;

    private final WhoisAssembler whoisAssembler;

    WhoisController(WhoisAssembler whoisAssembler) {
        this.whoisAssembler = whoisAssembler;
    }

    @GetMapping(value = "/whois")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des whois.",
            tags = { "Whois" })
    public CollectionModel<EntityModel<Whois>> getWhoisAll() {
        List<EntityModel<Whois>> whois = whoisService.getWhoisAll().stream()
                .map(whoisAssembler::toModel)
                .toList();

        return CollectionModel.of(whois, linkTo(methodOn(WhoisController.class).getWhoisAll()).withSelfRel());
    }

    @GetMapping(value = "/whois/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'un whois.",
            tags = { "Whois" })
    public ResponseEntity<?> getWhois(@PathVariable("id") final Integer id) {
        Whois whois = whoisService.getWhoisById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le whois %s.".formatted(id)));

        if (whois != null) {
            return ResponseEntity.ok(whoisAssembler.toModel(whois));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/whois")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer un whois.",
            tags = { "Whois" })
    public ResponseEntity<?> saveWhois(@RequestParam String descFr,
                                       @RequestParam String descEn,
                                       @RequestParam(required = false) String sex,
                                       @RequestParam(required = false) String ethnicity,
                                       @RequestParam(required = false, defaultValue = "0") Integer track1Start,
                                       @RequestParam(required = false, defaultValue = "0") Integer track1End,
                                       @RequestParam(required = false, defaultValue = "0") Integer track2Start,
                                       @RequestParam(required = false, defaultValue = "0") Integer track2End,
                                       @RequestParam(required = false, defaultValue = "0") Integer track3Start,
                                       @RequestParam(required = false, defaultValue = "0") Integer track3End) {
        if (descFr.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom français");
        }

        if (descEn.equals("")) {
            return ResponseEntity.badRequest().body("Saisir un nom anglais");
        }

        return ResponseEntity.ok(whoisService.saveWhois(Whois.builder()
                .descFr(descFr)
                .descEn(descEn)
                .sex(sex)
                .ethnicity(ethnicity)
                .track1Start(track1Start)
                .track1End(track1End)
                .track2Start(track2Start)
                .track2End(track2End)
                .track3Start(track3Start)
                .track3End(track3End)
                .build())
        );
    }

    @PutMapping(value = "/whois/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier un whois.",
            tags = { "Whois" })
    public ResponseEntity<?> putWhois(@PathVariable(name = "id") Integer id,
                                      @RequestParam(required = false) String descFr,
                                      @RequestParam(required = false) String descEn,
                                      @RequestParam(required = false) String sex,
                                      @RequestParam(required = false) String ethnicity,
                                      @RequestParam(required = false) Integer track1Start,
                                      @RequestParam(required = false) Integer track1End,
                                      @RequestParam(required = false) Integer track2Start,
                                      @RequestParam(required = false) Integer track2End,
                                      @RequestParam(required = false) Integer track3Start,
                                      @RequestParam(required = false) Integer track3End) {
        Whois whois = whoisService.getWhoisById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Whois introuvable"));

        if (whois != null) {
            if (!descFr.equals("")) {
                whois.setDescFr(descFr);
            }

            if (!descEn.equals("")) {
                whois.setDescEn(descEn);
            }

            if (!sex.equals("")) {
                whois.setSex(sex);
            }

            if (!ethnicity.equals("")) {
                whois.setEthnicity(ethnicity);
            }

            if (track1Start != null) {
                whois.setTrack1Start(track1Start);
            }
            if (track1End != null) {
                whois.setTrack1End(track1End);
            }

            if (track2Start != null) {
                whois.setTrack2Start(track2Start);
            }
            if (track2End != null) {
                whois.setTrack2End(track2End);
            }

            if (track3Start != null) {
                whois.setTrack3Start(track3Start);
            }
            if (track3End != null) {
                whois.setTrack3End(track3End);
            }

            whoisService.saveWhois(whois);

            return ResponseEntity.ok(whois);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/whois/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer un whois.",
            tags = { "Whois" })
    public ResponseEntity<?> deleteWhois(@PathVariable(name = "id") Integer id) {
        Whois whois = whoisService.getWhoisById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Whois introuvable"));

        if (whois != null) {
            whoisService.deleteWhois(whois);

            return ResponseEntity.ok("Whois supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
