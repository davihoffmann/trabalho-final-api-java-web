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
 * Created by Davi on 30/11/18.
 */
@Entity
@Table(name = "tb_endereco")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Relation(value = "endereco", collectionRelation = "enderecos")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "rua", "cidade", "estado", "cep"})
public class Endereco implements Serializable, Persistable<Long>, Identifiable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 250)
    @Column(nullable = false, length = 250)
    private String rua;

    @NotNull
    @Column(nullable = false, length = 250)
    @Size(min = 1, max = 250)
    private String cidade;

    @NotNull
    @Column(nullable = false, length = 2)
    @Size(min = 1, max = 2)
    private String estado;

    @NotNull
    @Column(nullable = false, length = 15)
    @Size(min = 1, max = 15)
    private String cep;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedTime;

    public Endereco(Long id, String rua, String cidade, String estado, String cep) {
        this.id = id;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
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

    public static Endereco of() {
        return new Endereco();
    }

    public static Endereco of(Long id, String rua, String cidade, String estado, String cep) {
        return new Endereco(id, rua, cidade, estado, cep);
    }

}
