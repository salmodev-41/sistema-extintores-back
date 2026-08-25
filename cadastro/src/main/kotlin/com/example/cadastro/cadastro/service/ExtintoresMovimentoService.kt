package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.MovimentoItemView
import com.example.cadastro.cadastro.dto.MovimentoView
import com.example.cadastro.cadastro.dto.NovoMovimentoForm
import com.example.cadastro.cadastro.exception.MovimentoValidationException
import com.example.cadastro.cadastro.exception.NotFoundException
import com.example.cadastro.cadastro.mapper.ItemViewMapper
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.MovimentoFormMapper
import com.example.cadastro.cadastro.mapper.MovimentoViewMapper
import com.example.cadastro.cadastro.model.MovimentoTipo
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoItemRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository

@Service
data class ExtintoresMovimentoService(
    private val repository: ExtintoresMovimentoRepository,
    private val movimentoFormMapper: MovimentoFormMapper,
    private val movimentoViewMapper: MovimentoViewMapper,
    private val empresasService: EmpresasService,
    private val movimentoItemRepository: ExtintoresMovimentoItemRepository,
    private val itemViewMapper: ItemViewMapper
) {

    fun buscarMovimento(id: Int): MovimentoView {
        val movimento = repository.findById(id)
            .orElseThrow { NotFoundException("Movimentação não encontrada") }
        return movimentoViewMapper.map(movimento)
    }

    fun listarMovimentacoes(): List<MovimentoView> {
        return repository.findAll().map { movimentoViewMapper.map(it) }
    }

    fun listarItensDaMovimentacao(movimentoId: Int): List<MovimentoItemView> {
        repository.findById(movimentoId)
            .orElseThrow { NotFoundException("Movimentação não encontrada") }

        return movimentoItemRepository
            .findByMovimentoId(movimentoId)
            .map { itemViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Movimentacoes"], allEntries = true)
    fun cadastrarMovimento(form: NovoMovimentoForm): MovimentoView {
        validarRegraDeNegocio(tipo = form.tipo, empresaDestinoCodigo = form.empresaDestinoCodigo) // <-- AQUI

        val movimento = movimentoFormMapper.map(form)
        val cadastrado = repository.save(movimento)
        return movimentoViewMapper.map(cadastrado)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Movimentacoes"], allEntries = true)
    fun atualizarMovimento(id: Int, form: NovoMovimentoForm): MovimentoView {
        validarRegraDeNegocio(tipo = form.tipo, empresaDestinoCodigo = form.empresaDestinoCodigo) // <-- E AQUI

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

    private fun validarRegraDeNegocio(tipo: MovimentoTipo, empresaDestinoCodigo: String) {
        when (tipo) {
            MovimentoTipo.T -> {
                if (empresaDestinoCodigo.isBlank()) {
                    throw MovimentoValidationException(
                        "Movimento do tipo Transferência (T) exige empresa_destino."
                    )
                }
            }
            MovimentoTipo.F, MovimentoTipo.S -> {
                if (empresaDestinoCodigo.isNotBlank()) {
                    throw MovimentoValidationException(
                        "empresa_destino só é aplicável a movimentos do tipo Transferência (T)."
                    )
                }
            }
        }
    }
}

