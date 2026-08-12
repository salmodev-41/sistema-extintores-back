package com.example.cadastro.cadastro.mapper

import com.example.cadastro.cadastro.dto.NovoExtintor
import com.example.cadastro.cadastro.exception.NotFoundException
import com.example.cadastro.cadastro.model.Extintores
import org.springframework.stereotype.Component
import com.example.cadastro.cadastro.repository.ExtintoresCategoriasRepository
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository

@Component
class ExtintorFormMapper(

    private val localizacaoRepository: ExtintoresLocalizacoesRepository,
    private val categoriasRepository: ExtintoresCategoriasRepository
) : Mapper<NovoExtintor, Extintores> {

    override fun map(t: NovoExtintor): Extintores {

        val localizacaoObjeto = localizacaoRepository.findById(t.localizacaoId)
            .orElseThrow { NotFoundException("Id da localização ${t.localizacaoId} não encontrada") }
        val tipoObjeto = categoriasRepository.findById(t.tipoId)
            .orElseThrow { NotFoundException("Id da cateogria de extintor ${t.tipoId} não encontrada") }

        return Extintores(
            numero = t.numero,
            situacao = t.situacao,
            tipo = tipoObjeto,
            cargaTotal = t.cargaTotal,
            localizacao = localizacaoObjeto,
            cargaVencimento = t.cargaVencimento,
            centroCusto = t.centroCusto,
            dataProxInspecao = t.dataProxInspecao
        )
    }
}