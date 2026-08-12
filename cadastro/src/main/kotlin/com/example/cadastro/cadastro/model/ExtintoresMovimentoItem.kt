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
@Table(name = "extintores_movimento_itens")
data class ExtintoresMovimentoItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "movimento_extintores_id")
    var movimento: ExtintoresMovimento? = null,

    @ManyToOne
    @JoinColumn(name = "extintor_numero")
    var extintor: Extintores? = null,

    @ManyToOne
    @JoinColumn(name = "destino")
    var destino: ExtintoresLocalizacoes? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 1)
    var tipoMovimentoItem: ItemMovimentoTipo? = null,

    @Column(name = "conferido")
    var conferido: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_retorno", length = 1)
    var tipoRetorno: TipoRetorno? = null,

    @Column(name = "carga_vencimento")
    var cargaVencimento: LocalDate? = null,

    @Column(name = "data_prox_inspecao")
    var dataProxInspecao: LocalDate? = null,

    @Column(name = "numero_substituto", length = 15)
    var numeroSubstituto: String = ""
)
