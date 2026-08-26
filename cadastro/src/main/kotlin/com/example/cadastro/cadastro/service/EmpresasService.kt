package com.example.cadastro.cadastro.service

import com.example.cadastro.cadastro.dto.EmpresasView
import com.example.cadastro.cadastro.dto.NovaEmpresa
import com.example.cadastro.cadastro.exception.NotFoundException
import jakarta.transaction.Transactional
import com.example.cadastro.cadastro.mapper.EmpresasFormMapper
import com.example.cadastro.cadastro.mapper.EmpresasViewMapper
import com.example.cadastro.cadastro.model.Empresas
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import com.example.cadastro.cadastro.repository.EmpresasRepository

@Service
class EmpresasService(
    private val repository: EmpresasRepository,
    private val empresasFormMapper: EmpresasFormMapper,
    private val empresasViewMapper: EmpresasViewMapper
) {

    fun buscarEmpresas(codigo: String): Empresas {
        return repository.findById(codigo)
            .orElseThrow { NotFoundException("Empresa não encontrada: $codigo") }
    }

    fun buscarEmpresaView(codigo: String): EmpresasView {
        val empresa = buscarEmpresas(codigo)
        return empresasViewMapper.map(empresa)
    }

    fun listarEmpresas(): List<EmpresasView> {
        return repository.findAll().map { empresasViewMapper.map(it) }
    }

    @Transactional
    @CacheEvict(cacheNames = ["Empresas"], allEntries = true)
    fun cadastrarEmpresas(form: NovaEmpresa): EmpresasView {
        val empresa = empresasFormMapper.map(form)
        val cadastrada = repository.save(empresa)
        return empresasViewMapper.map(cadastrada)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Empresas"], allEntries = true)
    fun atualizarEmpresas(codigo: String, form: NovaEmpresa): EmpresasView {
        val empresa = repository.findById(codigo)
            .orElseThrow { NotFoundException("Empresa não encontrada") }
        empresa.descricao = form.descricao

        return empresasViewMapper.map(empresa)
    }

    @Transactional
    @CacheEvict(cacheNames = ["Empresas"], allEntries = true)
    fun deletarEmpresas(codigo: String) {
        val empresa = repository.findById(codigo)
            .orElseThrow { NotFoundException("Empresa não encontrada") }
        repository.delete(empresa)
    }
}
