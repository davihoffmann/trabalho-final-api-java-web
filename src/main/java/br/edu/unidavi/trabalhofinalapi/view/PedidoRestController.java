package br.edu.unidavi.trabalhofinalapi.view;

import br.edu.unidavi.trabalhofinalapi.Application;
import br.edu.unidavi.trabalhofinalapi.domain.model.Pedido;
import br.edu.unidavi.trabalhofinalapi.service.PedidoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.function.Consumer;

import static br.edu.unidavi.trabalhofinalapi.domain.exception.EntityAreadyExistException.entityAreadyExist;
import static br.edu.unidavi.trabalhofinalapi.domain.exception.EntityNotFoundException.entityNotFoundException;
import static br.edu.unidavi.trabalhofinalapi.util.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by davi on 11/12/18.
 */
@RestController
@RequestMapping("/api/pedido")
public class PedidoRestController {

    @Autowired
    private PedidoService service;

    @Autowired
    private PedidoResourceAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resources<Resource<Pedido>>> findAll(Pageable pageable) {
        return ok(pagedResourcesAssembler.toResource(service.findAll(pageable), assembler));
    }

    @RequestMapping(method = GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Pedido>> findOne(@PathVariable Long id) {
        Pedido pedido = service.findOne(id).orElseThrow(entityAreadyExist("O Pedido não existe!"));;
        return ok(assembler.toResource(pedido));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Pedido>> gravar(@Valid @RequestBody PedidoPostInput input) {
        Pedido entity = Pedido.of();
        input.accept(entity);
        entity = service.save(entity);

        return ok(assembler.toResource(entity));
    }

    @RequestMapping(method = PATCH, value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Pedido>> edit(@PathVariable Long id, @Valid @RequestBody PedidoPatchInput input,
                                                  HttpServletRequest request)
    {
        Pedido pedido = service.findOne(id).orElseThrow(entityAreadyExist("O Pedido não existe!"));
        input.accept(pedido);
        service.save(pedido);

        return ok(assembler.toResource(pedido));
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        Pedido pedido = service.findOne(id).orElseThrow(entityAreadyExist("O Pedido não existe!"));
        service.delete(pedido);

        return noContent().build();
    }

    @Data
    static class PedidoPostInput implements Consumer<Pedido> {
        @NotNull
        private double total;

        private URI cliente;

        @Override
        public void accept(Pedido entity) {
            entity.setTotal(total);
            entity.setCliente(ClienteRestController.extractCliente(cliente));
        }
    }

    @Data
    static class PedidoPatchInput implements Consumer<Pedido> {

        @NotNull
        private double total;

        private URI cliente;

        @Override
        public void accept(Pedido pedido) {
            if (nonNull(total)) {
                pedido.setTotal(total);
            }
            if (nonNull(cliente)) {
                pedido.setCliente(ClienteRestController.extractCliente(cliente));
            }
        }
    }

    static Long extractPedidoId(String uri) {
        String id = (new UriTemplate("/api/pedido/{id}")).match(uri).get("id");
        checkArgument(id.matches("\\d+"), "Pedido não é válido!");

        return Long.valueOf(id);
    }

    static Long extractPedidoId(URI uri) {
        return extractPedidoId(uri.toASCIIString());
    }

    static Pedido extractPedido(URI uri) {
        return Application.getBean(PedidoService.class).findOne(extractPedidoId(uri))
                .orElseThrow(entityNotFoundException("Pedido não encontrado!"));
    }

    @Component
    public static class PedidoResourceAssembler implements ResourceAssembler<Pedido, Resource<Pedido>> {

        @Override
        public Resource<Pedido> toResource(Pedido pedido) {
            Resource<Pedido> resource = new Resource<>(pedido);

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PedidoRestController.class)
                    .findOne(pedido.getId())).withSelfRel());

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PedidoRestController.class)
                    .edit(pedido.getId(), null, null)).withRel("edit"));

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClienteRestController.class)
                    .findOne(pedido.getCliente().getId())).withRel("cliente"));

            return resource;
        }

    }
}