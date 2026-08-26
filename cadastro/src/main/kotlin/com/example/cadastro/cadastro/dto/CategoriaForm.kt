package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CategoriaForm(
    @field:NotBlank(message = "A descrição é obrigatória")
    @field:Size(max = 30, message = "A descrição deve ter no máximo 30 caracteres")
    val descricao: String,
    @field:NotBlank(message = "A unidade é obrigatória")
    @field:Size(max = 2, message = "A unidade deve ter no máximo 2 caracteres")
    val unidade: String,
    @field:Min(value = 1, message = "O período de inspeção deve ser maior que zero")
    val periodoInspecao: Int,
    @field:Min(value = 1, message = "O período de validade deve ser maior que zero")
    val periodoValidade: Int
)