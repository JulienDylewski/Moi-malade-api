package fr.limayrac.moimalade.disease.model;

import fr.limayrac.moimalade.disease.controller.DiseaseController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DiseaseListAssembler implements RepresentationModelAssembler<DiseaseList, EntityModel<DiseaseList>> {

    @Override
    public EntityModel<DiseaseList> toModel(DiseaseList disease) {
        return EntityModel.of(disease,
                linkTo(methodOn(DiseaseController.class).getDisease(disease.getId())).withSelfRel());
    }
}
