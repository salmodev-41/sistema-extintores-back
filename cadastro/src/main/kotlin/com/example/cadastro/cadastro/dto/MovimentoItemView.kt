package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.Extintores
import com.example.cadastro.cadastro.model.ExtintoresLocalizacoes
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import com.example.cadastro.cadastro.model.ItemMovimentoTipo
import com.example.cadastro.cadastro.model.TipoRetorno
import java.time.LocalDate

data class MovimentoItemView(
    var id: Int = 0,
    var movimento: ExtintoresMovimento? = null,
    var extintor: Extintores? = null,
    var destino: ExtintoresLocalizacoes? = null,
    var tipoMovimentoItem: ItemMovimentoTipo? = null,
    var conferido: Boolean = false,
    var tipoRetorno: TipoRetorno? = null,
    var cargaVencimento: LocalDate? = null,
    var dataProxInspecao: LocalDate? = null,
    var numeroSubstituto: String = ""
)