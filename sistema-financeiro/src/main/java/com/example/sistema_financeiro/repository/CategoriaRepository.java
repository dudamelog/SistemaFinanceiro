package com.example.sistema_financeiro.repository;

import com.example.sistema_financeiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

