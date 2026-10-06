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

@Entity
@Table(name = "extintores_localizacoes")
data class ExtintoresLocalizacoes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localizacao")
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "empresa_codigo")
    var empresa: Empresas? = null,

    @Column(name = "descricao", length = 200)
    var descricao: String = "",

    @Column(name = "centro_custo", length = 20)
    var centroCusto: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo" , length = 1)
    var tipo: LocalizacaoTipo? = null
)
