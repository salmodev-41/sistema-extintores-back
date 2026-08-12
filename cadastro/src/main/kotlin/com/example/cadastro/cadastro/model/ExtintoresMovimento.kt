package com.example.cadastro.cadastro.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "extintores_movimento")
data class ExtintoresMovimento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimento")
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "empresa_codigo")
    var empresa: Empresas? = null,

    @Column(name = "data_movimento")
    var data: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 1)
    var tipo: MovimentoTipo? = null,

    @ManyToOne
    @JoinColumn(name = "empresa_destino")
    var empresaDestino: Empresas? = null,
)
