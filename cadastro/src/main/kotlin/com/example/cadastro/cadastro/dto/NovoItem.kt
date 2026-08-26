package com.example.cadastro.cadastro.dto

import com.example.cadastro.cadastro.model.ItemMovimentoTipo
import com.example.cadastro.cadastro.model.TipoRetorno
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class NovoItem(
    @field:Positive(message = "O ID do movimento deve ser positivo")
    var movimentoId: Int,
    @field:NotBlank(message = "O número do extintor é obrigatório")
    var extintorNumero: String,
    @field:Positive(message = "O ID do destino deve ser positivo")
    var destinoId: Int = 0,
    var tipoMovimentoItem: ItemMovimentoTipo? = null,
    var conferido: Boolean = false,
    var tipoRetorno: TipoRetorno? = null,
    var cargaVencimento: LocalDate? = null,
    var dataProxInspecao: LocalDate? = null,
    @field:Size(max = 15, message = "O número do substituto deve ter no máximo 15 caracteres")
    var numeroSubstituto: String = ""
)