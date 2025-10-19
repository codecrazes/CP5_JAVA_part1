package br.com.fiap.supermarket.repository;

import br.com.fiap.supermarket.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}