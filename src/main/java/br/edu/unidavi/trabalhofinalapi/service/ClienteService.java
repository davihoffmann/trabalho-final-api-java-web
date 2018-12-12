package br.edu.unidavi.trabalhofinalapi.service;

import br.edu.unidavi.trabalhofinalapi.domain.model.Cliente;
import br.edu.unidavi.trabalhofinalapi.domain.repository.ClienteRepository;
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
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Page<Cliente> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Cliente> findOne(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cliente save(Cliente cli) {
        return repository.save(cli);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Cliente cliente) {
        repository.delete(cliente);
    }

//    public Optional<Cliente> findByCnpj(String cnpj) {
//        return Optional.ofNullable(repository.findByCnpj(cnpj));
//    }

}
