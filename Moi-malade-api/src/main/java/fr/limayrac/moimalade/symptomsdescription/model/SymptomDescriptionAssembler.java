package fr.limayrac.moimalade.symptomsdescription.model;

import fr.limayrac.moimalade.symptomsdescription.controller.SymptomDescriptionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SymptomDescriptionAssembler implements RepresentationModelAssembler<SymptomDescription, EntityModel<SymptomDescription>> {

    @Override
    public EntityModel<SymptomDescription> toModel(SymptomDescription symptomDescription) {
        return EntityModel.of(symptomDescription,
                linkTo(methodOn(SymptomDescriptionController.class).getSymptomDescription(symptomDescription.getId())).withSelfRel());
    }
}