package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.MovimentoItemView
import com.example.cadastro.cadastro.dto.NovoItem
import com.example.cadastro.cadastro.exception.MovimentoValidationException
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.ItemFormMapper
import com.example.cadastro.cadastro.mapper.ItemViewMapper
import com.example.cadastro.cadastro.model.ExtintorSituacao
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import com.example.cadastro.cadastro.model.ExtintoresMovimentoItem
import com.example.cadastro.cadastro.model.ItemMovimentoTipo
import com.example.cadastro.cadastro.model.MovimentoTipo
import com.example.cadastro.cadastro.model.TipoRetorno
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoItemRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository
import com.example.cadastro.cadastro.repository.ExtintoresRepository
import java.time.LocalDate

@Service
class MovimentoItemService(
    private val repository: ExtintoresMovimentoItemRepository,
    private val itemFormMapper: ItemFormMapper,
    private val itemViewMapper: ItemViewMapper,

    private val extintoresMovimentoRepository: ExtintoresMovimentoRepository,
    private val extintoresRepository: ExtintoresRepository,
    private val extintoresLocalizacoesRepository: ExtintoresLocalizacoesRepository
) {

    fun buscarMovimentoItem(id: Int): MovimentoItemView {
        val item = repository.findById(id)
            .orElseThrow { NotFoundException("Item do movimento não encontrado") }
        return itemViewMapper.map(item)
    }

    fun listarMovimentoItem(): List<MovimentoItemView> {
        return repository.findAll().map { itemViewMapper.map(it) }
    }

    fun listarItensPorMovimento(movimentoId: Int): List<MovimentoItemView> {
        return repository.findByMovimentoId(movimentoId)
            .map { itemViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["MovimentoItens"], allEntries = true)
    fun cadastrarMovimentoItem(form: NovoItem): MovimentoItemView {
        val movimento = extintoresMovimentoRepository.findById(form.movimentoId)
            .orElseThrow { IllegalArgumentException("Movimento com ID ${form.movimentoId} não encontrado") }

        val item = itemFormMapper.map(form)
        validarItem(movimento, item)

        val cadastrado = repository.save(item)
        aplicarEfeitosNoExtintor(movimento, cadastrado)

        return itemViewMapper.map(cadastrado)
    }

    @Transactional
    @CacheEvict(cacheNames = ["MovimentoItens"], allEntries = true)
    fun atualizarMovimentoItem(id: Int, form: NovoItem): MovimentoItemView {
        val item = repository.findById(id)
            .orElseThrow { NotFoundException("Item do movimento não encontrado") }

        val movimentoCompleto = extintoresMovimentoRepository.findById(form.movimentoId)
            .orElseThrow { IllegalArgumentException("Movimento com ID ${form.movimentoId} não encontrado") }

        val extintorCompleto = extintoresRepository.findById(form.extintorNumero)
            .orElseThrow { IllegalArgumentException("Extintor número ${form.extintorNumero} não encontrado") }

        val destinoCompleto = if (form.destinoId > 0) {
            extintoresLocalizacoesRepository.findById(form.destinoId)
                .orElseThrow { IllegalArgumentException("Localização de destino com ID ${form.destinoId} não encontrada") }
        } else {
            null
        }

        item.movimento = movimentoCompleto
        item.extintor = extintorCompleto
        item.destino = destinoCompleto
        item.tipoMovimentoItem = form.tipoMovimentoItem
        item.conferido = form.conferido
        item.tipoRetorno = form.tipoRetorno
        item.cargaVencimento = form.cargaVencimento
        item.dataProxInspecao = form.dataProxInspecao
        item.numeroSubstituto = form.numeroSubstituto

        validarItem(movimentoCompleto, item)
        aplicarEfeitosNoExtintor(movimentoCompleto, item)

        return itemViewMapper.map(item)
    }

    @Transactional
    @CacheEvict(cacheNames = ["MovimentoItens"], allEntries = true)
    fun deletarMovimentoItem(id: Int) {
        val item = repository.findById(id)
            .orElseThrow { NotFoundException("Item do movimento não encontrado") }
        repository.delete(item)
    }

    /**
     * Regra de negócio combinada:
     * - Movimento F: tipo do item é obrigatório (R ou I) e tipo_retorno deve ser coerente.
     * - Movimento S/T: tipo do item e tipo_retorno não se aplicam (devem ficar null).
     * - Movimento T: destino do item é obrigatório.
     * - numero_substituto só é válido quando tipo_retorno indica substituição.
     */
    private fun validarItem(movimento: ExtintoresMovimento, item: ExtintoresMovimentoItem) {
        when (movimento.tipo) {
            MovimentoTipo.F -> {
                if (item.tipoMovimentoItem == null) {
                    throw MovimentoValidationException(
                        "Item do extintor ${item.extintor?.numero}: tipo (R/I) é obrigatório em movimento F."
                    )
                }
                if (item.tipoRetorno != null && item.tipoRetorno != mapTipoParaRetorno(item.tipoMovimentoItem!!)) {
                    throw MovimentoValidationException(
                        "Item do extintor ${item.extintor?.numero}: tipo_retorno incoerente com o tipo do item."
                    )
                }
            }
            MovimentoTipo.S, MovimentoTipo.T -> {
                if (item.tipoMovimentoItem != null) {
                    throw MovimentoValidationException(
                        "Item do extintor ${item.extintor?.numero}: tipo não deve ser informado em movimento ${movimento.tipo}."
                    )
                }
                if (movimento.tipo == MovimentoTipo.T && item.destino == null) {
                    throw MovimentoValidationException(
                        "Item do extintor ${item.extintor?.numero}: destino é obrigatório em Transferência."
                    )
                }
            }
            null -> throw MovimentoValidationException("Movimento sem tipo definido.")
        }

        if (item.numeroSubstituto.isNotBlank() && item.tipoRetorno != TipoRetorno.S) {
            throw MovimentoValidationException(
                "Item do extintor ${item.extintor?.numero}: numero_substituto só é válido quando tipo_retorno = Substituído."
            )
        }
    }

    private fun mapTipoParaRetorno(tipo: ItemMovimentoTipo): TipoRetorno = when (tipo) {
        ItemMovimentoTipo.R -> TipoRetorno.R
        ItemMovimentoTipo.I -> TipoRetorno.I
    }

    /**
     * Aplica no Extintor os efeitos definidos pela regra de negócio, de acordo com o tipo do movimento.
     * Entidade gerenciada pelo JPA dentro da transação — dirty checking persiste as mudanças.
     */
    private fun aplicarEfeitosNoExtintor(movimento: ExtintoresMovimento, item: ExtintoresMovimentoItem) {
        val extintor = item.extintor ?: return
        val categoria = extintor.tipo

        when (movimento.tipo) {
            MovimentoTipo.F -> {
                when (item.tipoMovimentoItem) {
                    ItemMovimentoTipo.R -> {
                        val vencimento = item.cargaVencimento
                            ?: categoria?.periodoValidade?.let { LocalDate.now().plusMonths(it.toLong()) }
                        item.cargaVencimento = vencimento
                        extintor.cargaVencimento = vencimento
                    }
                    ItemMovimentoTipo.I -> {
                        val proxInspecao = item.dataProxInspecao
                            ?: categoria?.periodoInspecao?.let { LocalDate.now().plusMonths(it.toLong()) }
                        item.dataProxInspecao = proxInspecao
                        extintor.dataProxInspecao = proxInspecao
                    }
                    else -> {}
                }
            }
            MovimentoTipo.T -> {
                extintor.localizacao = item.destino
            }
            MovimentoTipo.S -> {
                // situacao permanece igual, sem alteração
            }
            null -> {}
        }

        if (item.tipoRetorno == TipoRetorno.S) {
            extintor.situacao = ExtintorSituacao.S
        }
    }
}
