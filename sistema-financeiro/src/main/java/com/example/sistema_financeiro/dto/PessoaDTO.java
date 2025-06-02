package com.example.sistema_financeiro.dto;

import com.example.sistema_financeiro.model.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Usando Record para DTO
public record PessoaDTO(
        Long codigo, // Incluído para retorno e atualização
        @NotNull @Size(min = 3, max = 100) String nome,
        @Valid EnderecoDTO endereco, // Usando EnderecoDTO
        @NotNull Boolean ativo
) {
}

