package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.NovoMovimentoForm
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import org.springframework.stereotype.Component
import com.example.cadastro.cadastro.service.EmpresasService

@Component
data class MovimentoFormMapper(

    private val empresasService: EmpresasService
) : Mapper<NovoMovimentoForm, ExtintoresMovimento> {

    override fun map(t: NovoMovimentoForm): ExtintoresMovimento {

        val empresaOrigem = empresasService.buscarEmpresas(t.empresaCodigo)
        val empresaDestino = empresasService.buscarEmpresas(t.empresaDestinoCodigo)

        return ExtintoresMovimento(
            empresa = empresaOrigem,
            data = t.data,
            tipo = t.tipo,
            empresaDestino = empresaDestino
        )
    }
}