package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.NovoItem
import com.example.cadastro.cadastro.model.ExtintoresMovimentoItem
import org.springframework.stereotype.Component
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository
import com.example.cadastro.cadastro.repository.ExtintoresRepository

@Component
class ItemFormMapper(
    private val extintoresMovimentoRepository: ExtintoresMovimentoRepository,
    private val extintoresRepository: ExtintoresRepository,
    private val extintoresLocalizacoesRepository: ExtintoresLocalizacoesRepository
) : Mapper<NovoItem, ExtintoresMovimentoItem> {

    override fun map(t: NovoItem): ExtintoresMovimentoItem {


        val movimentoCompleto = extintoresMovimentoRepository.findById(t.movimentoId)
            .orElseThrow { IllegalArgumentException("Movimento com ID ${t.movimentoId} não encontrado") }

        val extintorCompleto = extintoresRepository.findById(t.extintorNumero)
            .orElseThrow { IllegalArgumentException("Extintor número ${t.extintorNumero} não encontrado") }

        val destinoCompleto = if (t.destinoId > 0) {
            extintoresLocalizacoesRepository.findById(t.destinoId)
                .orElseThrow { IllegalArgumentException("Localização de destino com ID ${t.destinoId} não encontrada") }
        } else {
            null
        }


        return ExtintoresMovimentoItem(
            movimento = movimentoCompleto,
            extintor = extintorCompleto,
            destino = destinoCompleto,
            tipoMovimentoItem = t.tipoMovimentoItem,
            conferido = t.conferido,
            tipoRetorno = t.tipoRetorno,
            cargaVencimento = t.cargaVencimento,
            dataProxInspecao = t.dataProxInspecao,
            numeroSubstituto = t.numeroSubstituto
        )
    }
}