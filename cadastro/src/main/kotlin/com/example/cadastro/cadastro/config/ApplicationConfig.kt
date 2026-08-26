package com.example.cadastro.cadastro.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
class ApplicationConfig {

    @Value("\${app.admin.email}")
    private lateinit var adminEmail: String

    @Value("\${app.admin.password}")
    private lateinit var adminPassword: String

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val userDetails = User.withUsername(adminEmail)
            .password(passwordEncoder().encode(adminPassword))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(userDetails)
    }
}