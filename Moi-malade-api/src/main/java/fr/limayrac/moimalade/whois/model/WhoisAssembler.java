package fr.limayrac.moimalade.whois.model;

import fr.limayrac.moimalade.whois.controller.WhoisController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WhoisAssembler implements RepresentationModelAssembler<Whois, EntityModel<Whois>> {

    @Override
    public EntityModel<Whois> toModel(Whois whois) {
        return EntityModel.of(whois,
                linkTo(methodOn(WhoisController.class).getWhois(whois.getId())).withSelfRel());
    }
}
