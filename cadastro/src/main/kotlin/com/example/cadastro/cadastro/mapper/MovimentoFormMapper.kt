package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.NovoMovimentoForm
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import com.example.cadastro.cadastro.model.MovimentoTipo
import org.springframework.stereotype.Component
import com.example.cadastro.cadastro.service.EmpresasService

@Component
class MovimentoFormMapper(

    private val empresasService: EmpresasService
) : Mapper<NovoMovimentoForm, ExtintoresMovimento> {

    override fun map(t: NovoMovimentoForm): ExtintoresMovimento {

        val empresaOrigem = empresasService.buscarEmpresas(t.empresaCodigo)
        val empresaDestino = if (t.tipo == MovimentoTipo.T) {
            empresasService.buscarEmpresas(t.empresaDestinoCodigo)
        } else {
            null
        }

        return ExtintoresMovimento(
            empresa = empresaOrigem,
            data = t.data,
            tipo = t.tipo,
            empresaDestino = empresaDestino
        )
    }
}