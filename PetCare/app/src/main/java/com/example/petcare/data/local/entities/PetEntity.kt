package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val age: Int,
    val breed: String? = null,
    val imageUri: String? = null // <--- NUEVO: Ruta local de la foto de la mascota
)