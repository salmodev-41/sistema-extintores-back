package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.CategoriaForm
import com.example.cadastro.cadastro.model.ExtintoresCategorias
import org.springframework.stereotype.Component

@Component
class CategoriaFormMapper : Mapper<CategoriaForm, ExtintoresCategorias> {

    override fun map(t: CategoriaForm): ExtintoresCategorias {
        return ExtintoresCategorias(
            unidade = t.unidade,
            descricao = t.descricao,
            periodoInspecao = t.periodoInspecao,
            periodoValidade = t.periodoValidade
        )
    }
}