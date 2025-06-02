package com.example.sistema_financeiro.controller;

import com.example.sistema_financeiro.dto.PessoaDTO;
import com.example.sistema_financeiro.model.Endereco;
import com.example.sistema_financeiro.model.Pessoa;
import com.example.sistema_financeiro.repository.PessoaRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar pessoas
 */
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Lista todas as pessoas
     */
    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    /**
     * Cria uma nova pessoa
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody PessoaDTO pessoaDTO, HttpServletResponse response) {
        // Cria uma nova pessoa com os dados do DTO
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setAtivo(pessoaDTO.ativo());

        // Converte o endereço do DTO para entidade
        if (pessoaDTO.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(pessoaDTO.endereco().logradouro());
            endereco.setNumero(pessoaDTO.endereco().numero());
            endereco.setComplemento(pessoaDTO.endereco().complemento());
            endereco.setBairro(pessoaDTO.endereco().bairro());
            endereco.setCep(pessoaDTO.endereco().cep());
            endereco.setCidade(pessoaDTO.endereco().cidade());
            endereco.setEstado(pessoaDTO.endereco().estado());
            pessoa.setEndereco(endereco);
        }

        // Salva no banco
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // Cria URI para o recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(pessoaSalva.getCodigo())
                .toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(pessoaSalva);
    }

    /**
     * Busca uma pessoa pelo código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
        return pessoa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza uma pessoa existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody PessoaDTO pessoaDTO) {
        // Verifica se a pessoa existe
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(codigo);

        if (pessoaExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Obtém a pessoa existente
        Pessoa pessoa = pessoaExistente.get();

        // Atualiza os dados básicos
        pessoa.setNome(pessoaDTO.nome());
        pessoa.setAtivo(pessoaDTO.ativo());

        // Atualiza o endereço
        if (pessoaDTO.endereco() != null) {
            if (pessoa.getEndereco() == null) {
                pessoa.setEndereco(new Endereco());
            }

            pessoa.getEndereco().setLogradouro(pessoaDTO.endereco().logradouro());
            pessoa.getEndereco().setNumero(pessoaDTO.endereco().numero());
            pessoa.getEndereco().setComplemento(pessoaDTO.endereco().complemento());
            pessoa.getEndereco().setBairro(pessoaDTO.endereco().bairro());
            pessoa.getEndereco().setCep(pessoaDTO.endereco().cep());
            pessoa.getEndereco().setCidade(pessoaDTO.endereco().cidade());
            pessoa.getEndereco().setEstado(pessoaDTO.endereco().estado());
        }

        // Salva as alterações
        return ResponseEntity.ok(pessoaRepository.save(pessoa));
    }

    /**
     * Remove uma pessoa logicamente (desativa)
     */
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        // Verifica se a pessoa existe
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(codigo);

        if (pessoaExistente.isPresent()) {
            // Exclusão lógica - apenas marca como inativa
            Pessoa pessoa = pessoaExistente.get();
            pessoa.setAtivo(false);
            pessoaRepository.save(pessoa);
        }
    }
}
