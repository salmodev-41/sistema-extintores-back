package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.CategoriaForm
import com.example.cadastro.cadastro.dto.CategoriaView
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.CategoriaFormMapper
import com.example.cadastro.cadastro.mapper.CategoriaViewMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.ExtintoresCategoriasRepository

@Service
data class ExtintoresCategoriasService(
    private val repository: ExtintoresCategoriasRepository,
    private val categoriaFormMapper: CategoriaFormMapper,
    private val categoriaViewMapper: CategoriaViewMapper
) {

    fun buscarCategoria(id: Int): CategoriaView {
        val categoria = repository.findById(id)
            .orElseThrow { NotFoundException("Categoria não encontrada") }
        return categoriaViewMapper.map(categoria)
    }

    fun listarCategorias(): List<CategoriaView> {
        return repository.findAll().map { categoriaViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Categorias"], allEntries = true)
    fun cadastrarCategoria(form: CategoriaForm): CategoriaView {
        val categoria = categoriaFormMapper.map(form)
        val cadastrada = repository.save(categoria)
        return categoriaViewMapper.map(cadastrada)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Categorias"], allEntries = true)
    fun atualizarCategoria(id: Int, form: CategoriaForm): CategoriaView {
        val categoria = repository.findById(id)
            .orElseThrow { NotFoundException("Categoria não encontrada") }

        categoria.unidade = form.unidade
        categoria.descricao = form.descricao
        categoria.periodoInspecao = form.periodoInspecao
        categoria.periodoValidade = form.periodoValidade

        return categoriaViewMapper.map(categoria)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Categorias"], allEntries = true)
    fun deletarCategoria(id: Int) {
        val categoria = repository.findById(id)
            .orElseThrow { NotFoundException("Categoria não encontrada") }
        repository.delete(categoria)
    }
}

