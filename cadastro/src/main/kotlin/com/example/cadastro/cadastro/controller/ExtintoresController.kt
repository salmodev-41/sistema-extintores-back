package com.example.cadastro.cadastro.controller

import com.example.cadastro.cadastro.dto.ExtintorView
import com.example.cadastro.cadastro.dto.NovoExtintor
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
import com.example.cadastro.cadastro.service.ExtintoresService

@RestController
@RequestMapping("/extintores")
class ExtintoresController(
    private val service: ExtintoresService
) {

    @GetMapping
    fun listarExtintores(): ResponseEntity<List<ExtintorView>> {
        val lista = service.listarExtintores()
        return ResponseEntity.ok(lista)
    }

    @GetMapping("/{numero}")
    fun buscarExtintoresPorNumero(@PathVariable numero: String): ResponseEntity<ExtintorView> {
        val view = service.buscarNumeroExtintor(numero)
        return ResponseEntity.ok(view)
    }

    @PostMapping
    fun cadastrarExtintores(
        @RequestBody @Valid form: NovoExtintor,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<ExtintorView> {
        val extintorView = service.cadastrarExtintor(form)
        val uri = uriBuilder.path("/extintores/{numero}")
            .buildAndExpand(extintorView.numero)
            .toUri()

        return ResponseEntity.created(uri).body(extintorView)
    }

    @PutMapping("/{numero}")
    fun atualizarExtintores(
        @PathVariable numero: String,
        @RequestBody @Valid form: NovoExtintor
    ): ResponseEntity<ExtintorView> {
        val extintorAtualizado = service.atualizarExtintor(numero, form)
        return ResponseEntity.ok(extintorAtualizado)
    }

    @DeleteMapping("/{numero}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarExtintores(@PathVariable numero: String) {
        service.deletarExtintor(numero)
    }
}