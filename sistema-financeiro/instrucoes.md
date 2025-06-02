# Guia de Instruções - Sistema Financeiro

Este guia contém instruções detalhadas para configurar, executar e testar o Sistema Financeiro desenvolvido com Spring Boot, JPA, MySQL e Flyway.

## Requisitos

- JDK 21
- MySQL 8.0 ou superior
- Maven
- Insomnia ou Postman (para testes da API)
- Navegador web (para o frontend)

## Configuração do Ambiente

### 1. Banco de Dados

1. Certificar q o MySQL esteja instalado e em execução
2. Por padrão, a aplicação está configurada p:
   - URL: `jdbc:mysql://localhost:3306/sistema_financeiro`
   - Usuário: `root`
   - Senha: `root`
3. Se necessário, altere essas configurações no arquivo `src/main/resources/application.properties`

### 2. Compilação e Execução

1. Navegue até a pasta raiz do projeto (onde está o arquivo `pom.xml`)
2. Execute o comando para compilar e iniciar a aplicação:

```bash
mvn spring-boot:run
```

3. A aplicação será iniciada na porta 8080 e o Flyway criará automaticamente as tabelas e inserirá os dados iniciais

## Estrutura do Projeto

```
sistema-financeiro/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── sistema_financeiro/
│   │   │               ├── controller/
│   │   │               │   ├── CategoriaController.java
│   │   │               │   ├── LancamentoController.java
│   │   │               │   └── PessoaController.java
│   │   │               ├── dto/
│   │   │               │   ├── CategoriaDTO.java
│   │   │               │   ├── EnderecoDTO.java
│   │   │               │   ├── LancamentoDTO.java
│   │   │               │   └── PessoaDTO.java
│   │   │               ├── model/
│   │   │               │   ├── Categoria.java
│   │   │               │   ├── Endereco.java
│   │   │               │   ├── Lancamento.java
│   │   │               │   ├── Pessoa.java
│   │   │               │   └── TipoLancamento.java
│   │   │               ├── repository/
│   │   │               │   ├── CategoriaRepository.java
│   │   │               │   ├── LancamentoRepository.java
│   │   │               │   └── PessoaRepository.java
│   │   │               └── SistemaFinanceiroApplication.java
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       │       ├── V1__criar_tabelas.sql
│   │       │       └── V2__inserir_dados_iniciais.sql
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── sistema_financeiro/
├── frontend/
│   ├── index.html
│   └── style.css
│   └── app.js
└── pom.xml
```

## Testando a API com Insomnia

### Categorias

#### Listar Categorias
- **Método**: GET
- **URL**: `http://localhost:8080/categorias`

#### Buscar Categoria por Código
- **Método**: GET
- **URL**: `http://localhost:8080/categorias/{codigo}`

#### Criar Nova Categoria
- **Método**: POST
- **URL**: `http://localhost:8080/categorias`
- **Corpo** (JSON):
```json
{
  "nome": "Imposto"
}
```

### Pessoas

#### Listar Pessoas
- **Método**: GET
- **URL**: `http://localhost:8080/pessoas`

#### Buscar Pessoa por Código
- **Método**: GET
- **URL**: `http://localhost:8080/pessoas/{codigo}`

#### Criar Nova Pessoa
- **Método**: POST
- **URL**: `http://localhost:8080/pessoas`
- **Corpo** (JSON):
```json
{
  "nome": "Zé Maria",
  "endereco": {
    "logradouro": "Rua das Silvas",
    "numero": "25",
    "bairro": "Valentina",
    "cep": "58028-30",
    "cidade": "Joao Pessoa",
    "estado": "Paraiba"
  },
  "ativo": true
}
```

#### Atualizar Pessoa
- **Método**: PUT
- **URL**: `http://localhost:8080/pessoas/{codigo}`
- **Corpo**: Mesmo formato do POST

#### Remover Pessoa (Exclusão Lógica)
- **Método**: DELETE
- **URL**: `http://localhost:8080/pessoas/{codigo}`

### Lançamentos

#### Listar Lançamentos
- **Método**: GET
- **URL**: `http://localhost:8080/lancamentos`

#### Buscar Lançamento por Código
- **Método**: GET
- **URL**: `http://localhost:8080/lancamentos/{codigo}`

#### Criar Novo Lançamento
- **Método**: POST
- **URL**: `http://localhost:8080/lancamentos`
- **Corpo** (JSON):
```json
{
  "descricao": "Faculdade",
  "dataVencimento": "2024-11-10",
  "valor": 500,
  "tipo": "DESPESA",
  "categoria": {
    "codigo": 5
  },
  "pessoa": {
    "codigo": 1
  }
}
```

#### Atualizar Lançamento
- **Método**: PUT
- **URL**: `http://localhost:8080/lancamentos/{codigo}`
- **Corpo**: Mesmo formato do POST

#### Remover Lançamento
- **Método**: DELETE
- **URL**: `http://localhost:8080/lancamentos/{codigo}`


Observação: O frontend é uma demonstração visual e não está completamente integrado com a API. 

## Solução de Problemas

### Erro de Conexão com o Banco de Dados
- Verifique se o MySQL está em execução
- Confirme as credenciais no arquivo `application.properties`
- Certifique-se de que o usuário tem permissões para criar bancos e tabelas

### Erro ao Iniciar a Aplicação
- Verifique se a porta 8080 não está sendo usada por outro processo
- Certifique-se de que o JDK 21 está instalado e configurado corretamente