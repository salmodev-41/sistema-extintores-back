package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.LocalizacaoView
import com.example.cadastro.cadastro.dto.NovaLocalizacaoForm
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.LocalizacaoFormMapper
import com.example.cadastro.cadastro.mapper.LocalizacaoViewMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.EmpresasRepository
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository

@Service
data class ExtintoresLocalizacoesService(
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
        val localizacao = localizacaoFormMapper.map(nova)
        val localizacaoCadastrada = repository.save(localizacao)
        return localizacaoViewMapper.map(localizacaoCadastrada)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Localizacoes"], allEntries = true)
    fun atualizarLocalizacao(id: Int, nova: NovaLocalizacaoForm): LocalizacaoView {
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
}

