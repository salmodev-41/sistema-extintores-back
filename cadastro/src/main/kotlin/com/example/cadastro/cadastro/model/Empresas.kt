package com.example.cadastro.cadastro.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "empresas")
data class Empresas(
    @Id
    @Column(name = "codigo")
    var codigo: String? = null,

    @Column(name = "descricao", length = 200)
    var descricao: String = ""
)
