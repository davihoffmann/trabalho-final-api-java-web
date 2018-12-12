package br.edu.unidavi.trabalhofinalapi.domain.repository;

import br.edu.unidavi.trabalhofinalapi.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Davi on 11/12/18.
 */
public interface ProdutoRepository extends JpaSpecificationExecutor<Produto>, JpaRepository<Produto, Long> {
}
