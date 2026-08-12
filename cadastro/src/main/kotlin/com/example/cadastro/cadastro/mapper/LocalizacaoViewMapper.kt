package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.LocalizacaoView
import com.example.cadastro.cadastro.model.ExtintoresLocalizacoes
import org.springframework.stereotype.Component

@Component
 class LocalizacaoViewMapper : Mapper<ExtintoresLocalizacoes, LocalizacaoView> {

    override fun map(t: ExtintoresLocalizacoes): LocalizacaoView {
        return LocalizacaoView(
            id = t.id,
            empresa = t.empresa,
            descricao = t.descricao,
            centroCusto = t.centroCusto,
            tipo = t.tipo
        )
    }
}