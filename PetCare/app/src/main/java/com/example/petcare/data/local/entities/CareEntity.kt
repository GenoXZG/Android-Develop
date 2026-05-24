package com.example.petcare.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "care_logs",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")] // Indexamos para que las búsquedas por mascota sean veloces
)
data class CareEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val careType: String, // "Vacuna", "Baño", "Comida"
    val dateTimestamp: Long, // Guardamos la fecha como Unix Timestamp (milisegundos)
    val notes: String? = null
)