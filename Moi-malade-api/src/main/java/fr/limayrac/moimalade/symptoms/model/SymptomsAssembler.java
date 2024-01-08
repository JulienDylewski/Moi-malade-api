package fr.limayrac.moimalade.symptoms.model;

import fr.limayrac.moimalade.symptoms.controller.SymptomsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SymptomsAssembler implements RepresentationModelAssembler<Symptoms, EntityModel<Symptoms>>
{

    @Override
    public EntityModel<Symptoms> toModel(Symptoms symptoms) {
        return EntityModel.of(symptoms,
                linkTo(methodOn(SymptomsController.class).getSymptom(symptoms.getId())).withSelfRel());
    }
}