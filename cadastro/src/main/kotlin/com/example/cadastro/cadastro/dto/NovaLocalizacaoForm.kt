package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import com.example.cadastro.cadastro.model.LocalizacaoTipo

data class NovaLocalizacaoForm(

    val id: Int = 0,

    @field:NotBlank(message = "O código da empresa é obrigatório")
    val empresaCodigo: String,

    @field:NotBlank(message = "A descrição é obrigatória")
    @field:Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    val descricao: String,

    @field:NotBlank(message = "O centro de custo é obrigatório")
    @field:Size(max = 20, message = "O centro de custo deve ter no máximo 20 caracteres")
    val centroCusto: String,

    @field:NotNull(message = "O tipo de localização é obrigatório")
    val tipo: LocalizacaoTipo?
)