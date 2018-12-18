package br.edu.unidavi.trabalhofinalapi.view;

import br.edu.unidavi.trabalhofinalapi.Application;
import br.edu.unidavi.trabalhofinalapi.domain.model.Endereco;
import br.edu.unidavi.trabalhofinalapi.service.EnderecoService;
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
@RequestMapping("/api/endereco")
public class EnderecoRestController {

    @Autowired
    private EnderecoService service;

    @Autowired
    private EnderecoResourceAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Endereco> pagedResourcesAssembler;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resources<Resource<Endereco>>> findAll(Pageable pageable) {
        return ok(pagedResourcesAssembler.toResource(service.findAll(pageable), assembler));
    }

    @RequestMapping(method = GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Endereco>> findOne(@PathVariable Long id) {
        Endereco endereco = service.findOne(id).orElseThrow(entityAreadyExist("O Endereço não existe!"));
        return ok(assembler.toResource(endereco));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Endereco>> gravar(@Valid @RequestBody EnderecoPostInput input) {
        Endereco entity = Endereco.of();
        input.accept(entity);
        entity = service.save(entity);

        return ok(assembler.toResource(entity));
    }

    @RequestMapping(method = PATCH, value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Endereco>> edit(@PathVariable Long id, @Valid @RequestBody EnderecoPatchInput input,
                                                   HttpServletRequest request)
    {
        Endereco endereco = service.findOne(id).orElseThrow(entityAreadyExist("O Endereço não existe!"));
        input.accept(endereco);
        service.save(endereco);

        return ok(assembler.toResource(endereco));
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        Endereco endereco = service.findOne(id).orElseThrow(entityAreadyExist("O Endereço não existe!"));
        service.delete(endereco);

        return noContent().build();
    }

    @Data
    static class EnderecoPostInput implements Consumer<Endereco> {

        @Size(min = 1, max = 250)
        private String rua;

        @NotNull
        @Size(min = 1, max = 250)
        private String cidade;

        @NotNull
        @Size(min = 1, max = 2)
        private String estado;

        @NotNull
        @Size(min = 1, max = 15)
        private String cep;

        private URI cliente;

        @Override
        public void accept(Endereco entity) {
            entity.setRua(rua);
            entity.setCidade(cidade);
            entity.setEstado(estado);
            entity.setCep(cep);
            entity.setCliente(ClienteRestController.extractCliente(cliente));
        }
    }

    @Data
    static class EnderecoPatchInput implements Consumer<Endereco> {

        @Size(min = 1, max = 250)
        private String rua;

        @NotNull
        @Size(min = 1, max = 250)
        private String cidade;

        @NotNull
        @Size(min = 1, max = 2)
        private String estado;

        @NotNull
        @Size(min = 1, max = 15)
        private String cep;

        private URI cliente;

        @Override
        public void accept(Endereco endereco) {
            if (nonNull(rua)) {
                endereco.setRua(rua);
            }
            if (nonNull(cidade)) {
                endereco.setCidade(cidade);
            }
            if (nonNull(estado)) {
                endereco.setEstado(estado);
            }
            if(nonNull(cep)) {
                endereco.setCep(cep);
            }
            if(nonNull(cliente)) {
                endereco.setCliente(ClienteRestController.extractCliente(cliente));
            }
        }
    }

    static Long extractEnderecoId(String uri) {
        String id = (new UriTemplate("/api/endereco/{id}")).match(uri).get("id");
        checkArgument(id.matches("\\d+"), "Endereço não é válido!");

        return Long.valueOf(id);
    }

    static Long extractEnderecoId(URI uri) {
        return extractEnderecoId(uri.toASCIIString());
    }

    static Endereco extractEndereco(URI uri) {
        return Application.getBean(EnderecoService.class).findOne(extractEnderecoId(uri))
                .orElseThrow(entityNotFoundException("Endereço não encontrado!"));
    }

    @Component
    public static class EnderecoResourceAssembler implements ResourceAssembler<Endereco, Resource<Endereco>> {

        @Override
        public Resource<Endereco> toResource(Endereco endereco) {
            Resource<Endereco> resource = new Resource<>(endereco);

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(EnderecoRestController.class)
                    .findOne(endereco.getId())).withSelfRel());

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(EnderecoRestController.class)
                    .edit(endereco.getId(), null, null)).withRel("edit"));

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClienteRestController.class)
                    .findOne(endereco.getCliente().getId())).withRel("cliente"));

            return resource;
        }

    }
}