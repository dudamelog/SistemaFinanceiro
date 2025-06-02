CREATE TABLE categoria (
    codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pessoa (
    codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    logradouro VARCHAR(50),
    numero VARCHAR(10),
    complemento VARCHAR(50),
    bairro VARCHAR(50),
    cep VARCHAR(10),
    cidade VARCHAR(50),
    estado VARCHAR(50),
    ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE lancamento (
    codigo BIGINT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(100) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(19,2) NOT NULL,
    observacao VARCHAR(100),
    tipo VARCHAR(20) NOT NULL,
    codigo_categoria BIGINT NOT NULL,
    codigo_pessoa BIGINT NOT NULL,
    FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
    FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
