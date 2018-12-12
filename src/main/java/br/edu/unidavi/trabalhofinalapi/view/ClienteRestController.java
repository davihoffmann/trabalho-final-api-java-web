package br.edu.unidavi.trabalhofinalapi.view;

import br.edu.unidavi.trabalhofinalapi.Application;
import br.edu.unidavi.trabalhofinalapi.domain.model.Cliente;
import br.edu.unidavi.trabalhofinalapi.service.ClienteService;
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
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Date;
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
@RequestMapping("/api/cliente")
public class ClienteRestController {

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteResourceAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Cliente> pagedResourcesAssembler;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resources<Resource<Cliente>>> findAll(Pageable pageable) {
        return ok(pagedResourcesAssembler.toResource(service.findAll(pageable), assembler));
    }

    @RequestMapping(method = GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Cliente>> findOne(@PathVariable Long id) {
        Cliente cliente = service.findOne(id).orElseThrow(entityAreadyExist("O Cliente não existe!"));;
        return ok(assembler.toResource(cliente));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Cliente>> gravar(@Valid @RequestBody ClientePostInput input) {
        Cliente entity = Cliente.of();
        input.accept(entity);
        entity = service.save(entity);

        return ok(assembler.toResource(entity));
    }

    @RequestMapping(method = PATCH, value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Cliente>> edit(@PathVariable Long id, @Valid @RequestBody ClientePatchInput input,
                                                  HttpServletRequest request)
    {
        Cliente cliente = service.findOne(id).orElseThrow(entityAreadyExist("O Cliente não existe!"));
        input.accept(cliente);
        service.save(cliente);

        return ok(assembler.toResource(cliente));
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        Cliente cliente = service.findOne(id).orElseThrow(entityAreadyExist("O Cliente não existe!"));
        service.delete(cliente);

        return noContent().build();
    }

    @Data
    static class ClientePostInput implements Consumer<Cliente> {

        @NotNull
        @Size(min = 1, max = 100)
        private String cpf;

        @NotNull
        @Size(min = 1, max = 250)
        private String nome;

        @NotNull
        @Size(min = 1, max = 250)
        private Date dataNascimento;

        @Override
        public void accept(Cliente entity) {
            entity.setCpf(cpf);
            entity.setNome(nome);
            entity.setDataNascimento(dataNascimento);
        }
    }

    @Data
    static class ClientePatchInput implements Consumer<Cliente> {

        @NotNull
        @Size(min = 1, max = 100)
        private String cpf;

        @NotNull
        @Size(min = 1, max = 250)
        private String nome;

        @NotNull
        @Size(min = 1, max = 250)
        private Date dataNascimento;

        @Override
        public void accept(Cliente cliente) {
            if (nonNull(cpf)) {
                cliente.setCpf(cpf);
            }
            if (nonNull(nome)) {
                cliente.setNome(nome);
            }
            if (nonNull(dataNascimento)) {
                cliente.setDataNascimento(dataNascimento);
            }
        }
    }

    static Long extractClienteId(String uri) {
        String id = (new UriTemplate("/api/cliente/{id}")).match(uri).get("id");
        checkArgument(id.matches("\\d+"), "Cliente não é válido!");

        return Long.valueOf(id);
    }

    static Long extractClienteId(URI uri) {
        return extractClienteId(uri.toASCIIString());
    }

    static Cliente extractCliente(URI uri) {
        return Application.getBean(ClienteService.class).findOne(extractClienteId(uri))
                .orElseThrow(entityNotFoundException("Cliente não encontrado!"));
    }

    @Component
    public static class ClienteResourceAssembler implements ResourceAssembler<Cliente, Resource<Cliente>> {

        @Override
        public Resource<Cliente> toResource(Cliente cliente) {
            Resource<Cliente> resource = new Resource<>(cliente);

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClienteRestController.class)
                    .findOne(cliente.getId())).withSelfRel());

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClienteRestController.class)
                    .edit(cliente.getId(), null, null)).withRel("edit"));

            return resource;
        }

    }
}