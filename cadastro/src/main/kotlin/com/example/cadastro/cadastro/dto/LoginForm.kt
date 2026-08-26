package com.example.cadastro.cadastro.dto

import jakarta.validation.constraints.NotBlank

data class LoginForm(
    @field:NotBlank(message = "O e-mail é obrigatório")
    val email: String,
    @field:NotBlank(message = "A senha é obrigatória")
    val senha: String
)