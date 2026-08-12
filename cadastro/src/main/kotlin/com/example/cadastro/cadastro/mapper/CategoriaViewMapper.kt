package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.CategoriaView
import com.example.cadastro.cadastro.model.ExtintoresCategorias
import org.springframework.stereotype.Component

@Component
class CategoriaViewMapper : Mapper<ExtintoresCategorias, CategoriaView> {

    override fun map(t: ExtintoresCategorias): CategoriaView {
        return CategoriaView(
            id = t.id,
            unidade = t.unidade,
            descricao = t.descricao,
            periodoInspecao = t.periodoInspecao,
            periodoValidade = t.periodoValidade
        )
    }
}