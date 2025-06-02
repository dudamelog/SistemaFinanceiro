package com.example.sistema_financeiro.dto;

import jakarta.validation.constraints.Size;

// Usando Record para DTO (imutável e conciso)
public record EnderecoDTO(
        @Size(max = 50) String logradouro,
        @Size(max = 10) String numero,
        @Size(max = 50) String complemento,
        @Size(max = 50) String bairro,
        @Size(max = 10) String cep,
        @Size(max = 50) String cidade,
        @Size(max = 50) String estado
) {
}

