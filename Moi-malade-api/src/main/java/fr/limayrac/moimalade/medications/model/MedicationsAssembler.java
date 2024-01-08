package fr.limayrac.moimalade.medications.model;

import fr.limayrac.moimalade.medications.controller.MedicationsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MedicationsAssembler implements RepresentationModelAssembler<Medications, EntityModel<Medications>> {

    @Override
    public EntityModel<Medications> toModel(Medications medications) {
        return EntityModel.of(medications,
                linkTo(methodOn(MedicationsController.class).getMedication(medications.getId())).withSelfRel());
    }
}
