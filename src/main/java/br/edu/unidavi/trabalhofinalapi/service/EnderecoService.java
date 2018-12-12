package br.edu.unidavi.trabalhofinalapi.service;

import br.edu.unidavi.trabalhofinalapi.domain.model.Endereco;
import br.edu.unidavi.trabalhofinalapi.domain.repository.EnderecoRepository;
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
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public List<Endereco> findAll() {
        return repository.findAll();
    }

    public Page<Endereco> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Endereco> findOne(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Endereco save(Endereco endereco) {
        return repository.save(endereco);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Endereco endereco) {
        repository.delete(endereco);
    }

//    public Optional<Cliente> findByCnpj(String cnpj) {
//        return Optional.ofNullable(repository.findByCnpj(cnpj));
//    }

}
