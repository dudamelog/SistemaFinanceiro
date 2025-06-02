package com.example.sistema_financeiro.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Endereco {

    @Size(max = 50)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 50)
    private String complemento;

    @Size(max = 50)
    private String bairro;

    @Size(max = 10) // Ajustado para formato comum de CEP (ex: 00000-000)
    private String cep;

    @Size(max = 50)
    private String cidade;

    @Size(max = 50)
    private String estado;

}

