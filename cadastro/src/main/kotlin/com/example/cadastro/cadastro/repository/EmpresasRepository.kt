package com.example.cadastro.cadastro.repository

import com.example.cadastro.cadastro.model.Empresas
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmpresasRepository: JpaRepository<Empresas, String> {
}