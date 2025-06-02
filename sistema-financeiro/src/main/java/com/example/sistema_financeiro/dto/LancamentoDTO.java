package com.example.sistema_financeiro.dto;

import com.example.sistema_financeiro.model.TipoLancamento;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

// Usando Record para DTO
public record LancamentoDTO(
        Long codigo, // Incluído para retorno e atualização
        @NotNull String descricao,
        @NotNull LocalDate dataVencimento,
        LocalDate dataPagamento,
        @NotNull BigDecimal valor,
        String observacao,
        @NotNull TipoLancamento tipo,
        @NotNull CategoriaDTO categoria, // Usando CategoriaDTO
        @NotNull PessoaDTO pessoa // Usando PessoaDTO
) {
}

