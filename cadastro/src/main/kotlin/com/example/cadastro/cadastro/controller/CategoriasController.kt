package com.example.cadastro.cadastro.controller

import com.example.cadastro.cadastro.dto.CategoriaForm
import com.example.cadastro.cadastro.dto.CategoriaView
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import com.example.cadastro.cadastro.service.ExtintoresCategoriasService

@RestController
@RequestMapping("/categorias")
class CategoriasController (
    private val service : ExtintoresCategoriasService
){

    @GetMapping
    fun listarCategorias(): ResponseEntity<List<CategoriaView>> {
        val lista = service.listarCategorias()
        return ResponseEntity.ok(lista)
    }

    @GetMapping("/{id}")
    fun buscarCategoria(@PathVariable id: Int): ResponseEntity<CategoriaView> {
        val view = service.buscarCategoria(id)
        return ResponseEntity.ok(view)
    }

    @PostMapping
    fun cadastrarCategorias(
        @RequestBody @Valid form: CategoriaForm,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<CategoriaView> {
        val categoriaView = service.cadastrarCategoria(form)
        val uri = uriBuilder.path("/categorias/{id}")
            .buildAndExpand(categoriaView.id)
            .toUri()
        return ResponseEntity.created(uri).body(categoriaView)
    }

    @PutMapping("/{id}")
    fun atualizarCategoria(
        @PathVariable id: Int,
        @RequestBody @Valid novo: CategoriaForm
    ): ResponseEntity<CategoriaView> {
        val categoriaView = service.atualizarCategoria(id, novo)
        return ResponseEntity.ok(categoriaView)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarCategoria(@PathVariable id: Int){
        service.deletarCategoria(id)
    }
}