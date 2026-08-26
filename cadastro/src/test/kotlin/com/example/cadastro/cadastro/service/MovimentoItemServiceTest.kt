package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.MovimentoItemView
import com.example.cadastro.cadastro.dto.NovoItem
import com.example.cadastro.cadastro.exception.MovimentoValidationException
import com.example.cadastro.cadastro.mapper.ItemFormMapper
import com.example.cadastro.cadastro.mapper.ItemViewMapper
import com.example.cadastro.cadastro.model.Extintores
import com.example.cadastro.cadastro.model.ExtintoresLocalizacoes
import com.example.cadastro.cadastro.model.ExtintoresMovimento
import com.example.cadastro.cadastro.model.ExtintoresMovimentoItem
import com.example.cadastro.cadastro.model.MovimentoTipo
import com.example.cadastro.cadastro.repository.ExtintoresLocalizacoesRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoItemRepository
import com.example.cadastro.cadastro.repository.ExtintoresMovimentoRepository
import com.example.cadastro.cadastro.repository.ExtintoresRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MovimentoItemServiceTest {

    private val repository = mockk<ExtintoresMovimentoItemRepository>()
    private val itemFormMapper = mockk<ItemFormMapper>()
    private val itemViewMapper = mockk<ItemViewMapper>()
    private val movimentoRepository = mockk<ExtintoresMovimentoRepository>()
    private val extintoresRepository = mockk<ExtintoresRepository>()
    private val localizacoesRepository = mockk<ExtintoresLocalizacoesRepository>()

    private val service = MovimentoItemService(
        repository,
        itemFormMapper,
        itemViewMapper,
        movimentoRepository,
        extintoresRepository,
        localizacoesRepository
    )

    @Test
    fun `deve rejeitar item sem tipo em movimento de manutencao`() {
        val movimento = ExtintoresMovimento(tipo = MovimentoTipo.F)
        val item = ExtintoresMovimentoItem(movimento = movimento, extintor = Extintores("EXT-1"))
        val form = NovoItem(movimentoId = 1, extintorNumero = "EXT-1")

        every { movimentoRepository.findById(1) } returns Optional.of(movimento)
        every { itemFormMapper.map(form) } returns item

        val exception = assertFailsWith<MovimentoValidationException> {
            service.cadastrarMovimentoItem(form)
        }

        assertEquals("tipo (R/I) é obrigatório", exception.message?.substringAfter(": "))
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `deve exigir destino em transferencia`() {
        val movimento = ExtintoresMovimento(tipo = MovimentoTipo.T)
        val item = ExtintoresMovimentoItem(movimento = movimento, extintor = Extintores("EXT-1"))
        val form = NovoItem(movimentoId = 1, extintorNumero = "EXT-1")

        every { movimentoRepository.findById(1) } returns Optional.of(movimento)
        every { itemFormMapper.map(form) } returns item

        val exception = assertFailsWith<MovimentoValidationException> {
            service.cadastrarMovimentoItem(form)
        }

        assertEquals("destino é obrigatório em Transferência.", exception.message?.substringAfter(": "))
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `deve aplicar destino no extintor em transferencia valida`() {
        val destino = ExtintoresLocalizacoes(id = 7)
        val extintor = Extintores(numero = "EXT-1")
        val movimento = ExtintoresMovimento(tipo = MovimentoTipo.T)
        val item = ExtintoresMovimentoItem(
            movimento = movimento,
            extintor = extintor,
            destino = destino
        )
        val form = NovoItem(movimentoId = 1, extintorNumero = "EXT-1", destinoId = 7)
        val view = MovimentoItemView(id = 10)

        every { movimentoRepository.findById(1) } returns Optional.of(movimento)
        every { itemFormMapper.map(form) } returns item
        every { repository.save(item) } returns item
        every { itemViewMapper.map(item) } returns view

        val result = service.cadastrarMovimentoItem(form)

        assertEquals(view, result)
        assertEquals(destino, extintor.localizacao)
        verify(exactly = 1) { repository.save(item) }
    }
}
