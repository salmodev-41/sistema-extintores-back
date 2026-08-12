package com.example.cadastro.cadastro.controller

import com.example.cadastro.cadastro.security.JWTUtil
import com.example.cadastro.cadastro.dto.LoginForm
import com.example.cadastro.cadastro.dto.TokenView
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
data class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil
) {

    @PostMapping("/login")
    fun login(@RequestBody form: LoginForm): ResponseEntity<TokenView> {

        val loginToken = UsernamePasswordAuthenticationToken(form.email, form.senha)

        val authentication = authenticationManager.authenticate(loginToken)

        val jwtStr = jwtUtil.gerarToken(authentication.name)

        return ResponseEntity.ok(TokenView(jwtStr))
    }
}