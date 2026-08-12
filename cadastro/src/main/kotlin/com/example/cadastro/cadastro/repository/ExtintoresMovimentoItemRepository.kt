package com.example.cadastro.cadastro.repository

import com.example.cadastro.cadastro.model.ExtintoresMovimentoItem
import org.springframework.data.jpa.repository.JpaRepository

interface ExtintoresMovimentoItemRepository: JpaRepository<ExtintoresMovimentoItem, Int> {
}