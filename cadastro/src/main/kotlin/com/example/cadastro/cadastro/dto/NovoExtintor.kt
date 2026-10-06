package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.ExtintorSituacao
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class NovoExtintor(
    @field:NotBlank(message = "O número do extintor é obrigatório")
    @field:Size(max = 15, message = "O número do extintor deve ter no máximo 15 caracteres")
    val numero: String,
    @field:NotNull(message = "A situação é obrigatória")
    val situacao: ExtintorSituacao,
    @field:Positive(message = "O ID do tipo deve ser positivo")
    val tipoId: Int,
    @field:NotNull(message = "A carga total é obrigatória")
    val cargaTotal: BigDecimal,
    @field:Positive(message = "O ID da localização deve ser positivo")
    val localizacaoId: Int,
    @field:NotNull(message = "A data de vencimento da carga é obrigatória")
    val cargaVencimento: LocalDate,
    @field:NotNull(message = "A data da próxima inspeção é obrigatória")
    val dataProxInspecao: LocalDate,
    @field:Size(max = 20, message = "O centro de custo deve ter no máximo 20 caracteres")
    val centroCusto: String?
)