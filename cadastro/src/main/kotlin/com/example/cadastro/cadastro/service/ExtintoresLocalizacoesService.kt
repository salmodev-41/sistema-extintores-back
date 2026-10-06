package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.LocalizacaoView
import com.example.cadastro.cadastro.dto.NovaLocalizacaoForm
import com.example.cadastro.cadastro.exception.LocalizacaoValidationException
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.LocalizacaoFormMapper
import com.example.cadastro.cadastro.mapper.LocalizacaoViewMapper
import com.example.cadastro.cadastro.model.LocalizacaoTipo
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.EmpresasRepository
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository

@Service
class ExtintoresLocalizacoesService(
    private val repository: ExtintoresLocalizacoesRepository,
    private val empresasRepository: EmpresasRepository,
    private val localizacaoFormMapper: LocalizacaoFormMapper,
    private val localizacaoViewMapper: LocalizacaoViewMapper
) {

    fun buscarLocalizacao(id: Int): LocalizacaoView {
        val localizacao = repository.findById(id)
            .orElseThrow { NotFoundException("A localização não existe") }
        return localizacaoViewMapper.map(localizacao)
    }

    fun listarLocalizacoes(): List<LocalizacaoView> {
        return repository.findAll().map { localizacaoViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Localizacoes"], allEntries = true)
    fun cadastrarLocalizacao(nova: NovaLocalizacaoForm): LocalizacaoView {
        validarRegraDeNegocio(tipo = nova.tipo, centroCusto = nova.centroCusto) // <-- AQUI

        val localizacao = localizacaoFormMapper.map(nova)
        val localizacaoCadastrada = repository.save(localizacao)
        return localizacaoViewMapper.map(localizacaoCadastrada)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Localizacoes"], allEntries = true)
    fun atualizarLocalizacao(id: Int, nova: NovaLocalizacaoForm): LocalizacaoView {
        validarRegraDeNegocio(tipo = nova.tipo, centroCusto = nova.centroCusto) // <-- E AQUI

        val localizacao = repository.findById(id)
            .orElseThrow { NotFoundException("A localização não foi encontrada") }

        val novaEmpresa = empresasRepository.findById(nova.empresaCodigo)
            .orElseThrow { NotFoundException("Empresa não encontrada") }

        localizacao.empresa = novaEmpresa
        localizacao.descricao = nova.descricao
        localizacao.centroCusto = nova.centroCusto
        localizacao.tipo = nova.tipo

        return localizacaoViewMapper.map(localizacao)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Localizacoes"], allEntries = true)
    fun deletarLocalizacao(id: Int) {
        val localizacao = repository.findById(id)
            .orElseThrow { NotFoundException("A localização não foi encontrada") }
        repository.delete(localizacao)
    }

    private fun validarRegraDeNegocio(tipo: LocalizacaoTipo?, centroCusto: String?) {
        when (tipo) {
            LocalizacaoTipo.E -> {
                if (!centroCusto.isNullOrBlank()) {
                    throw LocalizacaoValidationException(
                        "Localização do tipo Estrutura (E) não pode ter centro de custo (veículo) vinculado."
                    )
                }
            }
            LocalizacaoTipo.V -> {
                if (centroCusto.isNullOrBlank()) {
                    throw LocalizacaoValidationException(
                        "Localização do tipo Veículo/Máquina (V) exige o centro de custo (id do veículo)."
                    )
                }
            }
            null -> {
            }
        }
    }
}