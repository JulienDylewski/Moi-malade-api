package fr.limayrac.moimalade.medicationsdescription.model;

import fr.limayrac.moimalade.medications.controller.MedicationsController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MedicationDescriptionAssembler implements RepresentationModelAssembler<MedicationDescription, EntityModel<MedicationDescription>> {

    @Override
    public EntityModel<MedicationDescription> toModel(MedicationDescription medicationDescription) {
        return EntityModel.of(medicationDescription,
                linkTo(methodOn(MedicationsController.class).getMedication(medicationDescription.getId())).withSelfRel());
    }
}
