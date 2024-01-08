package fr.limayrac.moimalade.testsprocedures.model;

import fr.limayrac.moimalade.testsprocedures.controller.TestsProceduresController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TestsProceduresAssembler implements RepresentationModelAssembler<TestsProcedures, EntityModel<TestsProcedures>>
{

    @Override
    public EntityModel<TestsProcedures> toModel(TestsProcedures testsProcedures) {
        return EntityModel.of(testsProcedures,
                linkTo(methodOn(TestsProceduresController.class).getTestProcedure(testsProcedures.getId())).withSelfRel());
    }
}
