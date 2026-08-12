package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.MovimentoView
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import org.springframework.stereotype.Component

@Component
  class MovimentoViewMapper : Mapper<ExtintoresMovimento, MovimentoView> {

    override fun map(t: ExtintoresMovimento): MovimentoView {
        return MovimentoView(
            id = t.id,
            empresa = t.empresa,
            data = t.data,
            tipo = t.tipo,
            empresaDestino = t.empresaDestino
        )
    }
}