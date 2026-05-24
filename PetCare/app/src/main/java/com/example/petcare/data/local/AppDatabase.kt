package com.example.petcare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petcare.data.local.dao.CareDao
import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.entities.CareEntity
import com.example.petcare.data.local.entities.PetEntity

@Database(entities = [PetEntity::class, CareEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun careDao(): CareDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Patrón Singleton para evitar que se abran múltiples instancias de la BD
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pet_care_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}