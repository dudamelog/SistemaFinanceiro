package com.example.sistema_financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@EqualsAndHashCode(of = "codigo")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 100)
    private String nome;

    @Embedded // Indica que Endereco é uma classe embutida
    private Endereco endereco;

    @NotNull
    private Boolean ativo;

}

