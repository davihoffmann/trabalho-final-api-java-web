package br.edu.unidavi.trabalhofinalapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Davi on 11/12/18.
 */
@Entity
@Table(name = "tb_item")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Relation(value = "item", collectionRelation = "itens")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "quantidade", "total"})
public class Item implements Serializable, Persistable<Long>, Identifiable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(nullable = false)
    private double total;

    @JsonIgnore
    @OneToOne(optional = false)
    private Produto produto;

    @JsonIgnore
    @OneToOne(optional = false)
    private Pedido pedido;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Item(Long id, Integer quantidade, double total, Produto produto) {
        this.id = id;
        this.quantidade = quantidade;
        this.total = total;
        this.produto = produto;
    }

    @Override
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }

    public static Item of() {
        return new Item();
    }

    public static Item of(Long id, Integer quantidade, double total, Produto produto) {
        return new Item(id, quantidade, total, produto);
    }

}
