package br.edu.unidavi.trabalhofinalapi.service;

import br.edu.unidavi.trabalhofinalapi.domain.model.Produto;
import br.edu.unidavi.trabalhofinalapi.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Davi on 11/12/18.
 */
@Service
@Transactional(readOnly = true)
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Page<Produto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Produto> findOne(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Produto save(Produto produto) {
        return repository.save(produto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Produto produto) {
        repository.delete(produto);
    }

    public Page<Produto> findByNomeContaining(String nome, Pageable pageable) {
        return repository.findByNomeContaining(nome, pageable);
    }

    public Page<Produto> findByMarcaContaining(String marca, Pageable pageable) {
        return repository.findByMarcaContaining(marca, pageable);
    }

    public Page<Produto> findByDataCriacao(Date data, Pageable pageable) {
        return repository.findByDataCriacao(data, pageable);
    }

}
