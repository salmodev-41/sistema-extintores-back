package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.MovimentoItemView
import com.example.cadastro.cadastro.model.ExtintoresMovimentoItem
import org.springframework.stereotype.Component

@Component
class ItemViewMapper : Mapper<ExtintoresMovimentoItem, MovimentoItemView> {

    override fun map(t: ExtintoresMovimentoItem): MovimentoItemView {
        return MovimentoItemView(
            id = t.id,
            movimento = t.movimento,
            extintor = t.extintor,
            destino = t.destino,
            tipoMovimentoItem = t.tipoMovimentoItem,
            conferido = t.conferido,
            tipoRetorno = t.tipoRetorno,
            cargaVencimento = t.cargaVencimento,
            dataProxInspecao = t.dataProxInspecao,
            numeroSubstituto = t.numeroSubstituto
        )
    }
}