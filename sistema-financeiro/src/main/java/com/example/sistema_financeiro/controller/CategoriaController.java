package com.example.sistema_financeiro.controller;

import com.example.sistema_financeiro.model.Categoria;
import com.example.sistema_financeiro.repository.CategoriaRepository;
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
 * Controller para gerenciar categorias
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Lista todas as categorias
     */
    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    /**
     * Cria uma nova categoria
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
        // Salva a categoria no banco
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        // Cria URI para o recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo())
                .toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(categoriaSalva);
    }

    /**
     * Busca uma categoria pelo código
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
        Optional<Categoria> categoria = categoriaRepository.findById(codigo);
        return categoria.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza uma categoria existente
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long codigo, @Valid @RequestBody Categoria categoria) {
        // Verifica se a categoria existe
        if (!categoriaRepository.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }

        // Define o código na categoria recebida
        categoria.setCodigo(codigo);

        // Salva a categoria atualizada
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return ResponseEntity.ok(categoriaSalva);
    }

    /**
     * Remove uma categoria
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> remover(@PathVariable Long codigo) {
        // Verifica se a categoria existe
        if (!categoriaRepository.existsById(codigo)) {
            return ResponseEntity.notFound().build();
        }

        // Remove a categoria
        categoriaRepository.deleteById(codigo);

        return ResponseEntity.noContent().build();
    }
}
