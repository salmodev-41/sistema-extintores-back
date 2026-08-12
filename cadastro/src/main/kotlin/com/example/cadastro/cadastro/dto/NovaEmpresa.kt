package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class NovaEmpresa (
    @field:NotBlank(message = "O código não pode ser nulo") var codigo: String,
    @field:NotBlank(message = "A descrição não pode estar em branco") var descricao: String
)