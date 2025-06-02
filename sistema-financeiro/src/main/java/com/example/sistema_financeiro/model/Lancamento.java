package com.example.sistema_financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamento")
@Getter
@Setter
@EqualsAndHashCode(of = "codigo")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull
    private String descricao;

    @NotNull
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotNull
    private BigDecimal valor;

    private String observacao;

    @NotNull
    @Enumerated(EnumType.STRING) // Grava o nome do enum (RECEITA/DESPESA)
    private TipoLancamento tipo;

    @NotNull
    @ManyToOne // Muitos lançamentos para uma categoria
    @JoinColumn(name = "codigo_categoria") // Chave estrangeira
    private Categoria categoria;

    @NotNull
    @ManyToOne // Muitos lançamentos para uma pessoa
    @JoinColumn(name = "codigo_pessoa") // Chave estrangeira
    private Pessoa pessoa;

}

