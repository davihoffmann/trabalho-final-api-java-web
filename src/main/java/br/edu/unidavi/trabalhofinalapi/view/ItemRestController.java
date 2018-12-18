package br.edu.unidavi.trabalhofinalapi.view;

import br.edu.unidavi.trabalhofinalapi.Application;
import br.edu.unidavi.trabalhofinalapi.domain.model.Item;
import br.edu.unidavi.trabalhofinalapi.domain.model.Pedido;
import br.edu.unidavi.trabalhofinalapi.service.ItemService;
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
@RequestMapping("/api/item")
public class ItemRestController {

    @Autowired
    private ItemService service;

    @Autowired
    private ItemResourceAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Item> pagedResourcesAssembler;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resources<Resource<Item>>> findAll(Pageable pageable) {
        return ok(pagedResourcesAssembler.toResource(service.findAll(pageable), assembler));
    }

    @RequestMapping(method = GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Item>> findOne(@PathVariable Long id) {
        Item item = service.findOne(id).orElseThrow(entityAreadyExist("O Item não existe!"));;
        return ok(assembler.toResource(item));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Item>> gravar(@Valid @RequestBody ItemPostInput input) {
        Item entity = Item.of();
        input.accept(entity);
        entity = service.save(entity);

        return ok(assembler.toResource(entity));
    }

    @RequestMapping(method = PATCH, value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<Resource<Item>> edit(@PathVariable Long id, @Valid @RequestBody ItemPatchInput input,
                                                 HttpServletRequest request)
    {
        Item item = service.findOne(id).orElseThrow(entityAreadyExist("O Item não existe!"));
        input.accept(item);
        service.save(item);

        return ok(assembler.toResource(item));
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @CrossOrigin
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        Item item = service.findOne(id).orElseThrow(entityAreadyExist("O Item não existe!"));
        service.delete(item);

        return noContent().build();
    }

    @Data
    static class ItemPostInput implements Consumer<Item> {
        @NotNull
        private Integer quantidade;

        @NotNull
        private double total;

        @NotNull
        private URI produto;

        @NotNull
        private URI pedido;

        @Override
        public void accept(Item entity) {
            entity.setQuantidade(quantidade);
            entity.setTotal(total);
            entity.setProduto(ProdutoRestController.extractProduto(produto));
            entity.setPedido(PedidoRestController.extractPedido(pedido));
        }
    }

    @Data
    static class ItemPatchInput implements Consumer<Item> {
        @NotNull
        private Integer quantidade;

        @NotNull
        private double total;

        @NotNull
        private URI produto;

        @NotNull
        private URI pedido;

        @Override
        public void accept(Item item) {
            if(nonNull(quantidade)) {
                item.setQuantidade(quantidade);
            }
            if (nonNull(total)) {
                item.setTotal(total);
            }
            if (nonNull(produto)) {
                item.setProduto(ProdutoRestController.extractProduto(produto));
            }
            if (nonNull(pedido)) {
                item.setPedido(PedidoRestController.extractPedido(pedido));
            }
        }
    }

    static Long extractItemId(String uri) {
        String id = (new UriTemplate("/api/item/{id}")).match(uri).get("id");
        checkArgument(id.matches("\\d+"), "Item não é válido!");

        return Long.valueOf(id);
    }

    static Long extractItemId(URI uri) {
        return extractItemId(uri.toASCIIString());
    }

    static Pedido extractItem(URI uri) {
        return Application.getBean(PedidoService.class).findOne(extractItemId(uri))
                .orElseThrow(entityNotFoundException("Item não encontrado!"));
    }

    @Component
    public static class ItemResourceAssembler implements ResourceAssembler<Item, Resource<Item>> {

        @Override
        public Resource<Item> toResource(Item item) {
            Resource<Item> resource = new Resource<>(item);

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ItemRestController.class)
                    .findOne(item.getId())).withSelfRel());

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ItemRestController.class)
                    .edit(item.getId(), null, null)).withRel("edit"));

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ProdutoRestController.class)
                    .findOne(item.getProduto().getId())).withRel("produto"));

            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PedidoRestController.class)
                    .findOne(item.getPedido().getId())).withRel("pedido"));

            return resource;
        }

    }
}