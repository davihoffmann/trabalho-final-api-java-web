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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@ToString(of = {"id", "nome", "descricao", "marca", "valor"})
public class Produto implements Serializable, Persistable<Long>, Identifiable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 250)
    @Size(min = 1, max = 250)
    private String nome;

    @NotNull
    @Column(nullable = false, length = 500)
    @Size(min = 1, max = 500)
    private String descricao;

    @NotNull
    @Column(nullable = false, length = 250)
    @Size(min = 1, max = 250)
    private String marca;

    @NotNull
    @Column(nullable = false)
    private double valor;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Produto(Long id, String nome, String descricao, String marca, double valor) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.marca = marca;
        this.valor = valor;
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

    public static Produto of() {
        return new Produto();
    }

    public static Produto of(Long id, String nome, String descricao, String marca, double valor) {
        return new Produto(id, nome, descricao, marca, valor);
    }

}
