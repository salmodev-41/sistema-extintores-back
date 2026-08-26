package com.example.cadastro.cadastro.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to exception.message.orEmpty()))

    @ExceptionHandler(MovimentoValidationException::class)
    fun handleBusinessValidation(exception: MovimentoValidationException): ResponseEntity<Map<String, String>> =
        ResponseEntity.badRequest().body(mapOf("message" to exception.message.orEmpty()))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBeanValidation(exception: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val message = exception.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(mapOf("message" to message))
    }
}
