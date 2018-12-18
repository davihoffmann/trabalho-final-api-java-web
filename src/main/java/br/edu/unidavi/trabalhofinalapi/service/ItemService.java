package br.edu.unidavi.trabalhofinalapi.service;

import br.edu.unidavi.trabalhofinalapi.domain.model.Item;
import br.edu.unidavi.trabalhofinalapi.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Davi on 11/12/18.
 */
@Service
@Transactional(readOnly = true)
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public List<Item> findAll() {
        return repository.findAll();
    }

    public Page<Item> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Item> findOne(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Item save(Item item) {
        return repository.save(item);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Item item) {
        repository.delete(item);
    }

//    public Optional<Cliente> findByCnpj(String cnpj) {
//        return Optional.ofNullable(repository.findByCnpj(cnpj));
//    }

}
