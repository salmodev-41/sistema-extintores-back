package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.NotBlank
import com.example.cadastro.cadastro.model.LocalizacaoTipo

data class NovaLocalizacaoForm(

    val id: Int = 0,

    @field:NotBlank(message = "O código da empresa é obrigatório")
    val empresaCodigo: String,

    @field:NotBlank(message = "A descrição é obrigatória")
    val descricao: String,

    @field:NotBlank(message = "O centro de custo é obrigatório")
    val centroCusto: String,

    @field:NotBlank(message = "O tipo de localização é obrigatório")
    val tipo: LocalizacaoTipo?
)