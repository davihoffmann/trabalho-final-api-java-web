package br.edu.unidavi.trabalhofinalapi.view;

import br.edu.unidavi.trabalhofinalapi.Application;
import br.edu.unidavi.trabalhofinalapi.domain.model.Produto;
import br.edu.unidavi.trabalhofinalapi.service.ProdutoService;
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
@RequestMapping("/api/produto")
public class ProdutoRestController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoResourceAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Produto> pagedResourcesAssembler;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resources<Resource<Produto>>> findAll(Pageable pageable) {
        return ok(pagedResourcesAssembler.toResource(service.findAll(pageable), assembler));
    }

    @RequestMapping(method = GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Produto>> findOne(@PathVariable Long id) {
        Produto produto = service.findOne(id).orElseThrow(entityAreadyExist("O Produto não existe!"));;
        return ok(assembler.toResource(produto));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Produto>> gravar(@Valid @RequestBody ProdutoPostInput input) {
        Produto entity = Produto.of();
        input.accept(entity);
        entity = service.save(entity);

        return ok(assembler.toResource(entity));
    }

    @RequestMapping(method = PATCH, value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Produto>> edit(@PathVariable Long id, @Valid @RequestBody ProdutoPatchInput input,
                                                  HttpServletRequest request)
    {
        Produto produto = service.findOne(id).orElseThrow(entityAreadyExist("O Produto não existe!"));
        input.accept(produto);
        service.save(produto);

        return ok(assembler.toResource(produto));
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        Produto produto = service.findOne(id).orElseThrow(entityAreadyExist("O Produto não existe!"));
        service.delete(produto);

        return noContent().build();
    }

    @Data
    static class ProdutoPostInput implements Consumer<Produto> {

        @NotNull
        @Size(min = 1, max = 250)
        private String nome;

        @NotNull
        @Size(min = 1, max = 500)
        private String descricao;

        @NotNull
        @Size(min = 1, max = 250)
        private String marca;

        @NotNull
        private double valor;

        @Override
        public void accept(Produto entity) {
            entity.setNome(nome);
            entity.setDescricao(descricao);
            entity.setMarca(marca);
            entity.setValor(valor);
        }
    }

    @Data
    static class ProdutoPatchInput implements Consumer<Produto> {

        @NotNull
        @Size(min = 1, max = 250)
        private String nome;

        @NotNull
        @Size(min = 1, max = 500)
        private String descricao;

        @NotNull
        @Size(min = 1, max = 250)
        private String marca;

        @NotNull
        private double valor;

        @Override
        public void accept(Produto produto) {
            if (nonNull(nome)) {
                produto.setNome(nome);
            }
            if (nonNull(descricao)) {
                produto.setDescricao(descricao);
            }
            if (nonNull(marca)) {
                produto.setMarca(marca);
            }
            if (nonNull(valor)) {
                produto.setValor(valor);
            }
        }
    }

    static Long extractProdutoId(String uri) {
        String id = (new UriTemplate("/api/produto/{id}")).match(uri).get("id");
        checkArgument(id.matches("\\d+"), "Produto não é válido!");

        return Long.valueOf(id);
    }

    static Long extractProdutoId(URI uri) {
        return extractProdutoId(uri.toASCIIString());
    }

    static Produto extractProduto(URI uri) {
        return Application.getBean(ProdutoService.class).findOne(extractProdutoId(uri))
                .orElseThrow(entityNotFoundException("Produto não encontrado!"));
    }

    @Component
    public static class ProdutoResourceAssembler implements ResourceAssembler<Produto, Resource<Produto>> {

        @Override
        public Resource<Produto> toResource(Produto produto) {
            Resource<Produto> resource = new Resource<>(produto);

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ProdutoRestController.class)
                    .findOne(produto.getId())).withSelfRel());

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ProdutoRestController.class)
                    .edit(produto.getId(), null, null)).withRel("edit"));

            return resource;
        }

    }
}