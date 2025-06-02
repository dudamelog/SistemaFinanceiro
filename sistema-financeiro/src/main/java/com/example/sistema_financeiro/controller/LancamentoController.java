package com.example.sistema_financeiro.controller;

import com.example.sistema_financeiro.dto.LancamentoDTO;
import com.example.sistema_financeiro.model.Categoria;
import com.example.sistema_financeiro.model.Lancamento;
import com.example.sistema_financeiro.model.Pessoa;
import com.example.sistema_financeiro.repository.CategoriaRepository;
import com.example.sistema_financeiro.repository.LancamentoRepository;
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
 * Controller para gerenciar lançamentos financeiros
 */
@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Lista todos os lançamentos
     */
    @GetMapping
    public List<Lancamento> listar() {
        return lancamentoRepository.findAll();
    }

    /**
     * Cria um novo lançamento
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody LancamentoDTO lancamentoDTO, HttpServletResponse response) {
        // Verifica se categoria e pessoa existem
        Optional<Categoria> categoria = categoriaRepository.findById(lancamentoDTO.categoria().codigo());
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamentoDTO.pessoa().codigo());

        if (categoria.isEmpty() || pessoa.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        // Cria o lançamento com os dados do DTO
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(lancamentoDTO.descricao());
        lancamento.setDataVencimento(lancamentoDTO.dataVencimento());
        lancamento.setDataPagamento(lancamentoDTO.dataPagamento());
        lancamento.setValor(lancamentoDTO.valor());
        lancamento.setObservacao(lancamentoDTO.observacao());
        lancamento.setTipo(lancamentoDTO.tipo());
        lancamento.setCategoria(categoria.get());
        lancamento.setPessoa(pessoa.get());

        // Salva no banco
        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        // Cria URI para o recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(lancamentoSalvo.getCodigo())
                .toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(lancamentoSalvo);
    }

    /**
     * Busca um lançamento pelo código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
        return lancamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza um lançamento existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid @RequestBody LancamentoDTO lancamentoDTO) {
        // Verifica se o lançamento existe
        Optional<Lancamento> lancamentoExistente = lancamentoRepository.findById(codigo);

        if (lancamentoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se categoria e pessoa existem
        Optional<Categoria> categoria = categoriaRepository.findById(lancamentoDTO.categoria().codigo());
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamentoDTO.pessoa().codigo());

        if (categoria.isEmpty() || pessoa.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Atualiza os dados do lançamento
        Lancamento lancamento = lancamentoExistente.get();
        lancamento.setDescricao(lancamentoDTO.descricao());
        lancamento.setDataVencimento(lancamentoDTO.dataVencimento());
        lancamento.setDataPagamento(lancamentoDTO.dataPagamento());
        lancamento.setValor(lancamentoDTO.valor());
        lancamento.setObservacao(lancamentoDTO.observacao());
        lancamento.setTipo(lancamentoDTO.tipo());
        lancamento.setCategoria(categoria.get());
        lancamento.setPessoa(pessoa.get());

        return ResponseEntity.ok(lancamentoRepository.save(lancamento));
    }

    /**
     * Remove um lançamento
     */
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        lancamentoRepository.deleteById(codigo);
    }
}
