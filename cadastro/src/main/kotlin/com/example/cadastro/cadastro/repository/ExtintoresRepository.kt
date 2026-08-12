package com.example.cadastro.cadastro.repository

import com.example.cadastro.cadastro.model.Extintores
import org.springframework.data.jpa.repository.JpaRepository

interface ExtintoresRepository: JpaRepository<Extintores, String> {
}