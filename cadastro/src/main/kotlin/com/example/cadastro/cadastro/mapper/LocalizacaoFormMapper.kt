package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.NovaLocalizacaoForm
import com.example.cadastro.cadastro.exception.NotFoundException
import com.example.cadastro.cadastro.model.ExtintoresLocalizacoes
import org.springframework.stereotype.Component
import com.example.cadastro.cadastro.repository.EmpresasRepository

@Component
class LocalizacaoFormMapper(
    private val empresasRepository: EmpresasRepository
) : Mapper<NovaLocalizacaoForm, ExtintoresLocalizacoes> {

    override fun map(t: NovaLocalizacaoForm): ExtintoresLocalizacoes {
        val empresaValido = t.empresaCodigo?: throw IllegalArgumentException("Id Inválido")
        val empresaObjeto = empresasRepository.findById(empresaValido)
            .orElseThrow { NotFoundException("Empresa com codigo $empresaValido não encontrada") }

        return ExtintoresLocalizacoes(
            id = t.id,
            empresa = empresaObjeto,
            descricao = t.descricao,
            centroCusto = t.centroCusto,
            tipo = t.tipo
        )
    }
}