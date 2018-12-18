package br.edu.unidavi.trabalhofinalapi.domain.repository;

import br.edu.unidavi.trabalhofinalapi.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by Davi on 11/12/18.
 */
public interface ProdutoRepository extends JpaSpecificationExecutor<Produto>, JpaRepository<Produto, Long> {

    Page<Produto> findByNomeContaining(String nome, Pageable pageable);

    Page<Produto> findByMarcaContaining(String marca, Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.createdTime = :data")
    Page<Produto> findByDataCriacao(@Param("data") Date data, Pageable pageable);

}
