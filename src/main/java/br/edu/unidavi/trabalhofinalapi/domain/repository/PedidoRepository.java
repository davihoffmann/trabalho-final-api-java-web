package br.edu.unidavi.trabalhofinalapi.domain.repository;

import br.edu.unidavi.trabalhofinalapi.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Davi on 11/12/18.
 */
public interface PedidoRepository extends JpaSpecificationExecutor<Pedido>, JpaRepository<Pedido, Long> {
}
