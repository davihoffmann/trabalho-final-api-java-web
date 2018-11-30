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
import java.util.Date;
import java.util.Objects;

/**
 * Created by Davi on 30/11/18.
 */
@Entity
@Table(name = "tb_cliente")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Relation(value = "cliente", collectionRelation = "clientes")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "cpf", "nome", "dataNascimento"})
public class Cliente implements Serializable, Persistable<Long>, Identifiable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String cpf;

    @NotNull
    @Column(nullable = false, length = 250)
    @Size(min = 1, max = 250)
    private String nome;

    @NotNull
    @Column(nullable = false)
    @Size(min = 1, max = 250)
    private Date dataNascimento;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Cliente(Long id, String cpf, String nome, Date dataNascimento) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    @Override
    public Long getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return Objects.isNull(cpf);
    }

    public static Cliente of() {
        return new Cliente();
    }

    public static Cliente of(Long id, String cpf, String nome, Date dataNascimento) {
        return new Cliente(id, cpf, nome, dataNascimento);
    }

}
