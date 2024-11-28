package com.example.miniprojetokotlin2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AddressDao {
    @Insert
    suspend fun insert(address: AddressEntity)

    @Query("SELECT * FROM address_table")
    suspend fun getAllAddresses(): List<AddressEntity>
}
