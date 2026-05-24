package com.example.petcare.data.local.dao

import androidx.room.*
import com.example.petcare.data.local.entities.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    // Usamos Flow. Esto hace que la consulta sea "reactiva".
    // Si agregas una mascota, la UI se actualizará automáticamente sin volver a consultar.

    @Query("SELECT * FROM pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: Int): PetEntity?
}