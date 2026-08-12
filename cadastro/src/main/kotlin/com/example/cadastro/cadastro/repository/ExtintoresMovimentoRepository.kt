package com.example.cadastro.cadastro.repository

import com.example.cadastro.cadastro.model.ExtintoresMovimento
import org.springframework.data.jpa.repository.JpaRepository

interface ExtintoresMovimentoRepository: JpaRepository<ExtintoresMovimento, Int> {
}