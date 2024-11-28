package com.example.miniprojetokotlin2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class AddressEntity(
    @PrimaryKey val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)