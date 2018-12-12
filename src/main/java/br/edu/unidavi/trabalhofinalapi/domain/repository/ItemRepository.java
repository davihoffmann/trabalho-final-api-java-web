package br.edu.unidavi.trabalhofinalapi.domain.repository;

import br.edu.unidavi.trabalhofinalapi.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Davi on 11/12/18.
 */
public interface ItemRepository extends JpaSpecificationExecutor<Item>, JpaRepository<Item, Long> {
}
