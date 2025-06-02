package com.example.sistema_financeiro.repository;

import com.example.sistema_financeiro.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}

