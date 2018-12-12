package br.edu.unidavi.trabalhofinalapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Objects;

/**
 * Created by Davi on 11/12/18.
 */
@Entity
@Table(name = "tb_produto")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Relation(value = "produto", collectionRelation = "produtos")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "total"})
public class Pedido implements Serializable, Persistable<Long>, Identifiable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private double total;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Cliente cliente;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<Item> itens = Lists.newLinkedList();

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Pedido(Long id, double total, Cliente cliente) {
        this.id = id;
        this.total = total;
        this.cliente = cliente;
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

    public static Pedido of() {
        return new Pedido();
    }

    public static Pedido of(Long id, double total, Cliente cliente) {
        return new Pedido(id, total, cliente);
    }

}
