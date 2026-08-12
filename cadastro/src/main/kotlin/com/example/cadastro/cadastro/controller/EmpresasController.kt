package com.example.cadastro.cadastro.controller

import com.example.cadastro.cadastro.dto.EmpresasView
import com.example.cadastro.cadastro.dto.NovaEmpresa
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
import com.example.cadastro.cadastro.service.EmpresasService


@RestController
@RequestMapping("/empresas")
data class EmpresasController(
    private val service: EmpresasService
) {

    @GetMapping("/{codigo}")
    fun buscarEmpresas(@PathVariable codigo: String): ResponseEntity<EmpresasView> {
        val view = service.buscarEmpresaView(codigo)
        return ResponseEntity.ok(view)
    }

    @GetMapping
    fun listarEmpresas(): ResponseEntity<List<EmpresasView>> {
        val lista = service.listarEmpresas()
        return ResponseEntity.ok(lista)
    }

    @PostMapping
    fun cadastrarEmpresas(
        @RequestBody @Valid empresa: NovaEmpresa,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<EmpresasView> {
        val empresasView = service.cadastrarEmpresas(empresa)
        val uri = uriBuilder.path("/empresas/{codigo}")
            .buildAndExpand(empresasView.codigo)
            .toUri()
        return ResponseEntity.created(uri).body(empresasView)
    }

    @PutMapping("/{codigo}")
    fun atualizarEmpresas(
        @PathVariable codigo: String,
        @RequestBody @Valid empresa: NovaEmpresa
    ): ResponseEntity<EmpresasView> {
        val empresasView = service.atualizarEmpresas(codigo, empresa)
        return ResponseEntity.ok(empresasView)
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarEmpresa(@PathVariable codigo: String) {
        service.deletarEmpresas(codigo)
    }
}