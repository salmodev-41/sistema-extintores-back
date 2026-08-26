package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.ExtintorView
import com.example.cadastro.cadastro.dto.NovoExtintor
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.ExtintorFormMapper
import com.example.cadastro.cadastro.mapper.ExtintorViewMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresCategoriasRepository
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository
import com.example.cadastro.cadastro.repository.ExtintoresRepository

@Service
class ExtintoresService(
    private val repository: ExtintoresRepository,
    private val categoriasRepository: ExtintoresCategoriasRepository,
    private val localizacoesRepository: ExtintoresLocalizacoesRepository,
    private val extintorFormMapper: ExtintorFormMapper,
    private val extintorViewMapper: ExtintorViewMapper
) {
    fun buscarNumeroExtintor(numero: String): ExtintorView {
        val extintor = repository.findById(numero)
            .orElseThrow { NotFoundException("Extintor não encontrado") }
        return extintorViewMapper.map(extintor)
    }

    fun listarExtintores(): List<ExtintorView> {
        return repository.findAll().map { extintorViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Extintores"], allEntries = true)
    fun cadastrarExtintor(form: NovoExtintor): ExtintorView {
        val extintor = extintorFormMapper.map(form)
        val extintorCadastrado = repository.save(extintor)
        return extintorViewMapper.map(extintorCadastrado)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Extintores"], allEntries = true)
    fun atualizarExtintor(numero: String, form: NovoExtintor): ExtintorView {
        val extintor = repository.findById(numero)
            .orElseThrow { NotFoundException("Extintor não encontrado") }
        val novaCategoria = categoriasRepository.findById(form.tipoId)
            .orElseThrow { NotFoundException("Categoria não encontrada") }
        val novaLocalizacao = localizacoesRepository.findById(form.localizacaoId)
            .orElseThrow { NotFoundException("Localização não encontrada") }

        extintor.situacao = form.situacao
        extintor.tipo = novaCategoria
        extintor.cargaTotal = form.cargaTotal
        extintor.localizacao = novaLocalizacao
        extintor.cargaVencimento = form.cargaVencimento
        extintor.dataProxInspecao = form.dataProxInspecao
        extintor.centroCusto = form.centroCusto

        return extintorViewMapper.map(extintor)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Extintores"], allEntries = true)
    fun deletarExtintor(numero: String) {
        val extintor = repository.findById(numero)
            .orElseThrow { NotFoundException("Extintor não encontrado") }
        repository.delete(extintor)
    }
}



