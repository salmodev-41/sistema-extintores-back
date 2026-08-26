package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.MovimentoTipo
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class NovoMovimentoForm(
    @field:NotBlank(message = "O código da empresa é obrigatório")
    var empresaCodigo: String,
    @field:NotNull(message = "A data é obrigatória")
    var data: LocalDate,
    @field:NotNull(message = "O tipo do movimento é obrigatório")
    var tipo: MovimentoTipo,
    @field:Size(max = 50, message = "O código da empresa destino deve ter no máximo 50 caracteres")
    var empresaDestinoCodigo: String
)