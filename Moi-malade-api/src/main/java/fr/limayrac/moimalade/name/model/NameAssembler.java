package fr.limayrac.moimalade.name.model;

import fr.limayrac.moimalade.name.controller.NameController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NameAssembler implements RepresentationModelAssembler<Name, EntityModel<Name>> {

    @Override
    public EntityModel<Name> toModel(Name name) {
        return EntityModel.of(name,
                linkTo(methodOn(NameController.class).getName(name.getId())).withSelfRel());
    }
}
