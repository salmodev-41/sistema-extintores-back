package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.MovimentoTipo
import java.time.LocalDate

data class NovoMovimentoForm(
    var empresaCodigo: String,
    var data: LocalDate,
    var tipo: MovimentoTipo,
    var empresaDestinoCodigo: String
)