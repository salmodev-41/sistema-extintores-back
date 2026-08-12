package com.example.cadastro.cadastro.mapper

interface Mapper<T, U> {
    fun map(t: T): U
}