package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.MovimentoItemView
import com.example.cadastro.cadastro.dto.NovoItem
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.ItemFormMapper
import com.example.cadastro.cadastro.mapper.ItemViewMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoItemRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository
import com.example.cadastro.cadastro.repository.ExtintoresRepository

@Service
data class MovimentoItemService(
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

    @Transactional
    @CacheEvict(cacheNames = ["MovimentoItens"], allEntries = true)
    fun cadastrarMovimentoItem(form: NovoItem): MovimentoItemView {
        val item = itemFormMapper.map(form)
        val cadastrado = repository.save(item)
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

        val destinoCompleto = extintoresLocalizacoesRepository.findById(form.destinoId)
            .orElseThrow { IllegalArgumentException("Localização de destino com ID ${form.destinoId} não encontrada") }

        item.movimento = movimentoCompleto
        item.extintor = extintorCompleto
        item.destino = destinoCompleto
        item.tipoMovimentoItem = form.tipoMovimentoItem
        item.conferido = form.conferido
        item.tipoRetorno = form.tipoRetorno
        item.cargaVencimento = form.cargaVencimento
        item.dataProxInspecao = form.dataProxInspecao
        item.numeroSubstituto = form.numeroSubstituto

        return itemViewMapper.map(item)
    }

    @Transactional
    @CacheEvict(cacheNames = ["MovimentoItens"], allEntries = true)
    fun deletarMovimentoItem(id: Int) {
        val item = repository.findById(id)
            .orElseThrow { NotFoundException("Item do movimento não encontrado") }
        repository.delete(item)
    }
}

