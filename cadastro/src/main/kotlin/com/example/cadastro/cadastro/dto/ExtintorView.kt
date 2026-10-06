package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.ExtintorSituacao
import com.example.cadastro.cadastro.model.ExtintoresCategorias
import java.math.BigDecimal
import java.time.LocalDate

data class ExtintorView(
    val numero: String? = null,
    val tipo: ExtintoresCategorias? = null,
    val cargaTotal: BigDecimal? = null,
    val cargaVencimento: LocalDate? = null,
    val dataProxInspecao: LocalDate? = null,
    val localizacao: LocalizacaoView? = null,
    val situacao: ExtintorSituacao? = null,
    val centroCusto: String? = null
)