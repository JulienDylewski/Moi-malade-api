package fr.limayrac.moimalade.disease.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.limayrac.moimalade.description.model.Description;
import fr.limayrac.moimalade.description.service.DescriptionService;
import fr.limayrac.moimalade.disease.model.*;
import fr.limayrac.moimalade.disease.service.DiseaseListService;
import fr.limayrac.moimalade.disease.service.DiseaseService;
import fr.limayrac.moimalade.disease.service.DiseaseSymptomService;
import fr.limayrac.moimalade.exception.RessourceNotFoundException;
import fr.limayrac.moimalade.medications.model.Medications;
import fr.limayrac.moimalade.medications.service.MedicationsService;
import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescription;
import fr.limayrac.moimalade.medicationsdescription.service.MedicationDescriptionService;
import fr.limayrac.moimalade.name.model.Name;
import fr.limayrac.moimalade.name.service.NameService;
import fr.limayrac.moimalade.symptoms.model.Symptoms;
import fr.limayrac.moimalade.symptoms.service.SymptomsService;
import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescription;
import fr.limayrac.moimalade.symptomsdescription.service.SymptomDescriptionService;
import fr.limayrac.moimalade.testsprocedures.model.TestsProcedures;
import fr.limayrac.moimalade.testsprocedures.service.TestsProceduresService;
import fr.limayrac.moimalade.whois.model.Whois;
import fr.limayrac.moimalade.whois.service.WhoisService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private DiseaseListService diseaseListService;

    @Autowired
    private NameService nameService;

    @Autowired
    private DescriptionService descriptionService;

    @Autowired
    private MedicationsService medicationsService;

    @Autowired
    private DiseaseSymptomService diseaseSymptomService;

    @Autowired
    private MedicationDescriptionService medicationDescriptionService;

    @Autowired
    private SymptomDescriptionService symptomDescriptionService;

    @Autowired
    private TestsProceduresService testsProceduresService;

    @Autowired
    private SymptomsService symptomsService;

    @Autowired
    private WhoisService whoisService;

    private final DiseaseAssembler diseaseAssembler;
    private final DiseaseListAssembler diseaseListAssembler;

    DiseaseController(DiseaseAssembler diseaseAssembler,
                      DiseaseListAssembler diseaseListAssembler) {
        this.diseaseAssembler = diseaseAssembler;
        this.diseaseListAssembler = diseaseListAssembler;
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet d'obtenir les maladies possible.",
            tags = { "Disease" })
    public List<Disease> searchDisease(@RequestParam(name = "sex") String sex,
                                       @RequestParam(name = "ethnicity") String ethnicity,
                                       @RequestParam(name = "age") Integer age,
                                       @RequestParam(name = "symptoms") String matrixJson) throws IllegalArgumentException,
            JsonProcessingException {
        // Convertir la représentation JSON de la matrice en une matrice Java
        String matrixDecoded = URLDecoder.decode(matrixJson, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        int[][] matrix = objectMapper.readValue(matrixDecoded, int[][].class);

        /*
        Premier tri, on récupère les maladies en fonction du sexe et où on a pas l'info
        la meme chose sur l'ethinicitéé et la meme chose sur l'age
        */
        List<Disease> firstTri = diseaseService.getBySexAgeEthnicity(sex, ethnicity, age);
        List<Disease> result = new ArrayList<>();

        for (Disease disease : firstTri) {
            int relevanceScore = 0;

            for (DiseaseSymptom diseaseSymptom : disease.getDiseaseSymptoms()) {
                for (int[] pair : matrix) {
                    if (pair[0] == diseaseSymptom.getSymptom().getId()) {
                        int difference = Math.abs(pair[1] - diseaseSymptom.getPainIndex());
                        relevanceScore += (100 - difference);
                    }
                }
            }

            if (relevanceScore > 0) {
                disease.setRelevanceScore(relevanceScore);
                double percentage = (double) relevanceScore / matrix.length;
                disease.setPercentage(percentage);
                result.add(disease);
            }
        }

        result = result.stream()
                .sorted(Comparator.comparing(Disease::getRelevanceScore).reversed())
                .limit(result.size() > 5 && result.get(0).getPercentage() >= 90.0 ? 3 : 5)
                .toList();

        return result;
    }

    @GetMapping(value = "/search2")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet d'obtenir les maladies possible.",
            tags = { "Disease" })
    public List<Disease> searchDisease2(@RequestParam(name = "sex") String sex,
                                       @RequestParam(name = "ethnicity") String ethnicity,
                                       @RequestParam(name = "age") Integer age,
                                       @RequestParam(name = "symptoms") String matrixJson) throws IllegalArgumentException,
            JsonProcessingException {
        // Convertir la représentation JSON de la matrice en une matrice Java
        String matrixDecoded = URLDecoder.decode(matrixJson, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        int[][] matrix = objectMapper.readValue(matrixDecoded, int[][].class);

        List<Disease> firstTri = diseaseService.getBySexAgeEthnicity(sex, ethnicity, age);
        List<Disease> result = new ArrayList<>();

        for (Disease disease : firstTri) {
            int relevanceScore = 0;
            int matchingSymptoms = 0;

            for (DiseaseSymptom diseaseSymptom : disease.getDiseaseSymptoms()) {
                for (int[] pair : matrix) {
                    if (pair[0] == diseaseSymptom.getSymptom().getId()) {
                        int difference = Math.abs(pair[1] - diseaseSymptom.getPainIndex());
                        relevanceScore += (100 - difference);
                        matchingSymptoms++;
                        break;
                    }
                }
            }

            if (matchingSymptoms > 0) {
                double averageRelevanceScore = (double) relevanceScore / matchingSymptoms;
                disease.setRelevanceScore2(averageRelevanceScore);
                result.add(disease);
            }
        }

        result = result.stream()
                .sorted(Comparator.comparing(Disease::getRelevanceScore2).reversed())
                .limit(5)
                .toList();

        return result;
    }

    @GetMapping(value = "/disease")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer la liste des disease.",
            tags = { "Disease" })
    public CollectionModel<EntityModel<DiseaseList>> getAllDisease() {
        List<EntityModel<DiseaseList>> disease = diseaseListService.getAll().stream()
                .map(diseaseListAssembler::toModel)
                .toList();

        return CollectionModel.of(disease, linkTo(methodOn(DiseaseController.class).getAllDisease()).withSelfRel());
    }

    @GetMapping(value = "/disease/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de récuperer les détails d'une disease.",
            tags = { "Disease" })
    public ResponseEntity<?> getDisease(@PathVariable("id") final Integer id) {
        Disease disease = diseaseService.getDiseaseById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la disease %s.".formatted(id)));

        if (disease != null) {
            return ResponseEntity.ok(diseaseAssembler.toModel(disease));
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @PostMapping("/disease")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de créer une disease.",
            tags = { "Disease" })
    public ResponseEntity<?> saveDisease(@RequestParam(name = "nameId") Integer nameId,
                                         @RequestParam(name = "descriptionId") Integer descriptionId,
                                         @RequestParam(name = "medicationDescId") Integer medicationDescId,
                                         @RequestParam(name = "symptomDescId") Integer symptomDescId,
                                         @RequestParam(name = "whoisId") Integer whoisId,
                                         @RequestParam(name = "medicationList") String medicationList,
                                         @RequestParam(name = "procedureList") String procedureList,
                                         @RequestParam(name = "symptomsList") String symptomsList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Name name = nameService.getNameById(nameId)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le name %s.".formatted(nameId)));
        Description description = descriptionService.getDescriptionById(descriptionId)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la description %s.".formatted(descriptionId)));
        MedicationDescription medicationDescription = medicationDescriptionService.getMedicationDescriptionById(medicationDescId)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver la medication desc %s.".formatted(medicationDescId)));
        SymptomDescription symptomDescription = symptomDescriptionService.getSymptomDescriptionById(symptomDescId)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le symptom desc %s.".formatted(symptomDescId)));
        Whois whois = whoisService.getWhoisById(whoisId)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver le whois %s.".formatted(whoisId)));

        if (name == null) {
            return ResponseEntity.badRequest().body("Saisir un name");
        }

        if (descriptionId == null) {
            return ResponseEntity.badRequest().body("Saisir une description");
        }

        if (medicationDescription == null) {
            return ResponseEntity.badRequest().body("Saisir une medication description");
        }

        if (symptomDescription == null) {
            return ResponseEntity.badRequest().body("Saisir un symptomDescription");
        }

        if (whois == null) {
            return ResponseEntity.badRequest().body("Saisir un whois");
        }

        if (medicationList == null) {
            return ResponseEntity.badRequest().body("Saisir une liste de medication");
        }

        String medicationMatrixDecoded = URLDecoder.decode(medicationList, StandardCharsets.UTF_8);
        String procedureMatrixDecoded = URLDecoder.decode(procedureList, StandardCharsets.UTF_8);
        String symptomsMatrixDecoded = URLDecoder.decode(symptomsList, StandardCharsets.UTF_8);

        int[] medicationMatrix = objectMapper.readValue(medicationMatrixDecoded, int[].class);
        int[] procedureMatrix = objectMapper.readValue(procedureMatrixDecoded, int[].class);
        int[][] symptomsMatrix = objectMapper.readValue(symptomsMatrixDecoded, int[][].class);

        List<Medications> diseaseMedications = new ArrayList<>();
        List<TestsProcedures> diseaseProcedures = new ArrayList<>();
        List<DiseaseSymptom> diseaseSymptoms = new ArrayList<>();

        for (int pair : medicationMatrix) {
            Optional<Medications> medications = medicationsService.getMedicationById(pair);
            medications.ifPresent(diseaseMedications::add);
        }

        for (int pair : procedureMatrix) {
            Optional<TestsProcedures> procedures = testsProceduresService.getTestProcedureById(pair);
            procedures.ifPresent(diseaseProcedures::add);
        }

        Disease newDisease = diseaseService.saveDisease(Disease.builder()
                .name(name)
                .description(description)
                .medicationDescription(medicationDescription)
                .symptomDescription(symptomDescription)
                .whois(whois)
                .medicationsList(diseaseMedications)
                .testsProceduresList(diseaseProcedures)
                .diseaseSymptoms(diseaseSymptoms)
                .build());

        for (int[] pair : symptomsMatrix) {
            Optional<Symptoms> symptom = symptomsService.getSymptomById(pair[0]);

            if (symptom.isPresent()) {
                DiseaseSymptom ds = diseaseSymptomService.saveDiseaseSymptom(DiseaseSymptom.builder()
                        .disease(newDisease)
                        .symptom(symptom.get())
                        .painIndex(pair[1])
                        .build());

                diseaseSymptoms.add(ds);
            }
        }

        newDisease.setDiseaseSymptoms(diseaseSymptoms);

        return ResponseEntity.ok(diseaseService.saveDisease(newDisease));
    }

    @PutMapping(value = "/disease/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de modifier une disease.",
            tags = { "Disease" })
    public ResponseEntity<?> putDisease(@PathVariable(name = "id") Integer id,
                                        @RequestParam(name = "nameId", required = false) Integer nameId,
                                        @RequestParam(name = "descriptionId", required = false) Integer descriptionId,
                                        @RequestParam(name = "medicationDescId", required = false) Integer medicationDescId,
                                        @RequestParam(name = "symptomDescId", required = false) Integer symptomDescId,
                                        @RequestParam(name = "whoisId", required = false) Integer whoisId,
                                        @RequestParam(name = "medicationList", required = false) String medicationList,
                                        @RequestParam(name = "procedureList", required = false) String procedureList,
                                        @RequestParam(name = "symptomsList", required = false) String symptomsList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Disease disease = diseaseService.getDiseaseById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Disease introuvable"));

        if (disease != null) {
            if (nameId != null) {
                Optional<Name> name = nameService.getNameById(nameId);
                name.ifPresent(disease::setName);
            }

            if (descriptionId != null) {
                Optional<Description> description = descriptionService.getDescriptionById(descriptionId);
                description.ifPresent(disease::setDescription);
            }

            if (medicationDescId != null) {
                Optional<MedicationDescription> medicationDesc = medicationDescriptionService.getMedicationDescriptionById(medicationDescId);
                medicationDesc.ifPresent(disease::setMedicationDescription);
            }

            if (symptomDescId != null) {
                Optional<SymptomDescription> symptomDescription = symptomDescriptionService.getSymptomDescriptionById(symptomDescId);
                symptomDescription.ifPresent(disease::setSymptomDescription);
            }

            if (whoisId != null) {
                Optional<Whois> whois = whoisService.getWhoisById(whoisId);
                whois.ifPresent(disease::setWhois);
            }

            if (medicationList != null) {
                String medicationMatrixDecoded = URLDecoder.decode(medicationList, StandardCharsets.UTF_8);
                int[] medicationMatrix = objectMapper.readValue(medicationMatrixDecoded, int[].class);
                List<Medications> diseaseMedications = new ArrayList<>();

                for (int pair : medicationMatrix) {
                    Optional<Medications> medications = medicationsService.getMedicationById(pair);
                    medications.ifPresent(diseaseMedications::add);
                }

                disease.setMedicationsList(diseaseMedications);
            }

            if (procedureList != null) {
                String procedureMatrixDecoded = URLDecoder.decode(procedureList, StandardCharsets.UTF_8);
                int[] procedureMatrix = objectMapper.readValue(procedureMatrixDecoded, int[].class);
                List<TestsProcedures> diseaseProcedures = new ArrayList<>();

                for (int pair : procedureMatrix) {
                    Optional<TestsProcedures> procedures = testsProceduresService.getTestProcedureById(pair);
                    procedures.ifPresent(diseaseProcedures::add);
                }

                disease.setTestsProceduresList(diseaseProcedures);
            }

            if (symptomsList != null) {
                String symptomsMatrixDecoded = URLDecoder.decode(symptomsList, StandardCharsets.UTF_8);
                int[][] symptomsMatrix = objectMapper.readValue(symptomsMatrixDecoded, int[][].class);
                List<DiseaseSymptom> diseaseSymptoms = new ArrayList<>();

                for (DiseaseSymptom diseaseSymptom : disease.getDiseaseSymptoms()) {
                    diseaseSymptomService.deleteDiseaseSymptom(diseaseSymptom);
                }

                for (int[] pair : symptomsMatrix) {
                    Optional<Symptoms> symptom = symptomsService.getSymptomById(pair[0]);

                    if (symptom.isPresent()) {
                        DiseaseSymptom ds = diseaseSymptomService.saveDiseaseSymptom(DiseaseSymptom.builder()
                                .disease(disease)
                                .symptom(symptom.get())
                                .painIndex(pair[1])
                                .build());

                        diseaseSymptoms.add(ds);
                    }
                }

                disease.setDiseaseSymptoms(diseaseSymptoms);
            }

            diseaseService.saveDisease(disease);

            return ResponseEntity.ok(disease);
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }

    @DeleteMapping(value = "/disease/{id}")
    @Operation(summary = "Nécessite une clé API dans le header.",
            description = "Cette méthode permet de supprimer une disease.",
            tags = { "Disease" })
    public ResponseEntity<?> deleteDisease(@PathVariable(name = "id") Integer id) {
        Disease disease = diseaseService.getDiseaseById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Disease introuvable"));

        if (disease != null) {
            diseaseService.deleteDisease(disease);

            return ResponseEntity.ok("Disease supprime");
        }

        return ResponseEntity.badRequest().body(
                "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées."
        );
    }
}
