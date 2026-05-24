package com.example.petcare.data.local.dao

import androidx.room.*
import com.example.petcare.data.local.entities.CareEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CareDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCareLog(care: CareEntity)

    // Obtiene el historial de cuidados de una mascota específica, del más reciente al más viejo
    @Query("SELECT * FROM care_logs WHERE petId = :petId ORDER BY dateTimestamp DESC")
    fun getCareLogsForPet(petId: Int): Flow<List<CareEntity>>
}