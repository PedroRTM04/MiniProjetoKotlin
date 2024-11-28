package com.example.miniprojetokotlin2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AddressEntity::class], version = 1)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
}
