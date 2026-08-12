package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.ItemMovimentoTipo
import com.example.cadastro.cadastro.model.TipoRetorno
import java.time.LocalDate

data class NovoItem(
    var movimentoId: Int,
    var extintorNumero: String,
    var destinoId: Int,
    var tipoMovimentoItem: ItemMovimentoTipo,
    var conferido: Boolean = false,
    var tipoRetorno: TipoRetorno,
    var cargaVencimento: LocalDate,
    var dataProxInspecao: LocalDate,
    var numeroSubstituto: String = ""
)