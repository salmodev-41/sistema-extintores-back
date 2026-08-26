package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class NovaEmpresa (
    @field:NotBlank(message = "O código não pode ser nulo")
    @field:Size(max = 15, message = "O código deve ter no máximo 15 caracteres")
    var codigo: String,
    @field:NotBlank(message = "A descrição não pode estar em branco")
    @field:Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    var descricao: String
)