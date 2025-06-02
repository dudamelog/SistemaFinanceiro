package com.example.sistema_financeiro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Usando Record para DTO
public record CategoriaDTO(
        Long codigo, // Incluído para retorno
        @NotNull @Size(min = 3, max = 50) String nome
) {
}

