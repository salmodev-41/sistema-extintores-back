package com.example.cadastro.cadastro.controller

import com.example.cadastro.cadastro.dto.LocalizacaoView
import com.example.cadastro.cadastro.dto.NovaLocalizacaoForm
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
import com.example.cadastro.cadastro.service.ExtintoresLocalizacoesService

@RestController
@RequestMapping("/localizacoes")
class LocalizacaoController(
    private val service: ExtintoresLocalizacoesService
){
    @GetMapping("/{id}")
    fun buscarLocalizacao(@PathVariable id: Int): ResponseEntity<LocalizacaoView> {
        val view = service.buscarLocalizacao(id)
        return ResponseEntity.ok(view)
    }

    @GetMapping
    fun listarLocalizacao(): ResponseEntity<List<LocalizacaoView>> {
        val lista = service.listarLocalizacoes()
        return ResponseEntity.ok(lista)
    }

    @PostMapping
    fun cadastrarLocalizacao(
        @RequestBody @Valid form: NovaLocalizacaoForm,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<LocalizacaoView> {
        val localizacaoView = service.cadastrarLocalizacao(form)
        val uri = uriBuilder.path("/localizacoes/{id}")
            .buildAndExpand(localizacaoView.id)
            .toUri()
        return ResponseEntity.created(uri).body(localizacaoView)
    }

    @PutMapping("/{id}")
    fun atualizarLocalizacao(
        @PathVariable id: Int,
        @RequestBody @Valid form: NovaLocalizacaoForm
    ): ResponseEntity<LocalizacaoView> {
        val localizacoesView = service.atualizarLocalizacao(id, form)
        return ResponseEntity.ok(localizacoesView)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarLocalizacao(@PathVariable id: Int){
        service.deletarLocalizacao(id)
    }
}