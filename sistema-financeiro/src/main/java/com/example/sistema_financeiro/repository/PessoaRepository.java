package com.example.sistema_financeiro.repository;

import com.example.sistema_financeiro.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}

