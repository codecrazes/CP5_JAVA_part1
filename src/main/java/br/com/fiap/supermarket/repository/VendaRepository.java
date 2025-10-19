package br.com.fiap.supermarket.repository;

import br.com.fiap.supermarket.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
