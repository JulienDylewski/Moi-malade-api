package fr.limayrac.moimalade.description.model;

import fr.limayrac.moimalade.description.controller.DescriptionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DescriptionAssembler implements RepresentationModelAssembler<Description, EntityModel<Description>> {

    @Override
    public EntityModel<Description> toModel(Description description) {
        return EntityModel.of(description,
                linkTo(methodOn(DescriptionController.class).getDescription(description.getId())).withSelfRel());
    }
}
