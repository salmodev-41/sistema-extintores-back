package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.MovimentoView
import com.example.cadastro.cadastro.dto.NovoMovimentoForm
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.MovimentoFormMapper
import com.example.cadastro.cadastro.mapper.MovimentoViewMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository

@Service
data class ExtintoresMovimentoService(
    private val repository: ExtintoresMovimentoRepository,
    private val movimentoFormMapper: MovimentoFormMapper,
    private val movimentoViewMapper: MovimentoViewMapper,
    private val empresasService: EmpresasService
) {

    fun buscarMovimento(id: Int): MovimentoView {
        val movimento = repository.findById(id)
            .orElseThrow { NotFoundException("Movimentação não encontrada") }
        return movimentoViewMapper.map(movimento)
    }


    fun listarMovimentacoes(): List<MovimentoView> {
        return repository.findAll().map { movimentoViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Movimentacoes"], allEntries = true)
    fun cadastrarMovimento(form: NovoMovimentoForm): MovimentoView {
        val movimento = movimentoFormMapper.map(form)
        val cadastrado = repository.save(movimento)
        return movimentoViewMapper.map(cadastrado)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Movimentacoes"], allEntries = true)
    fun atualizarMovimento(id: Int, form: NovoMovimentoForm): MovimentoView {
        val movimento = repository.findById(id)
            .orElseThrow { NotFoundException("Movimentação não encontrada") }

        val empresaOrigem = empresasService.buscarEmpresas(form.empresaCodigo)
        val empresaDestino = empresasService.buscarEmpresas(form.empresaDestinoCodigo)


        movimento.empresa = empresaOrigem
        movimento.data = form.data
        movimento.tipo = form.tipo
        movimento.empresaDestino = empresaDestino

        return movimentoViewMapper.map(movimento)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Movimentacoes"], allEntries = true)
    fun deletarMovimento(id: Int) {
        val movimento = repository.findById(id)
            .orElseThrow { NotFoundException("Movimentação não encontrada") }
        repository.delete(movimento)
    }
}

