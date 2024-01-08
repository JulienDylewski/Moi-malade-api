package fr.limayrac.moimalade.security.model;

import fr.limayrac.moimalade.security.controller.ApiKeyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiKeyAssembler implements RepresentationModelAssembler<ApiKey, EntityModel<ApiKey>> {

    @Override
    public EntityModel<ApiKey> toModel(ApiKey apiKey) {
        return EntityModel.of(apiKey,
                linkTo(methodOn(ApiKeyController.class).getApiKey(apiKey.getId())).withSelfRel());
    }
}
