package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.CareDao
import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.entities.CareEntity
import com.example.petcare.data.local.entities.PetEntity
import kotlinx.coroutines.flow.Flow

// Inyectamos los DAOs por el constructor. Esto facilita el testing más adelante.
class PetRepository(
    private val petDao: PetDao,
    private val careDao: CareDao
) {

    // ==========================================
    // OPERACIONES DE MASCOTAS
    // ==========================================

    // Retorna un Flow. La UI se suscribirá a esto y se actualizará automáticamente
    // si agregas a tu Siamés o a tu gato blanco con gris.
    val allPets: Flow<List<PetEntity>> = petDao.getAllPets()

    suspend fun getPetById(petId: Int): PetEntity? {
        return petDao.getPetById(petId)
    }

    suspend fun insertPet(pet: PetEntity) {
        // Aquí en el futuro podrías agregar un try-catch para enviarlo a tu API
        petDao.insertPet(pet)
    }

    suspend fun deletePet(pet: PetEntity) {
        petDao.deletePet(pet)
    }

    // ==========================================
    // OPERACIONES DE CUIDADOS (HISTORIAL)
    // ==========================================

    fun getCareLogsForPet(petId: Int): Flow<List<CareEntity>> {
        return careDao.getCareLogsForPet(petId)
    }

    suspend fun insertCareLog(care: CareEntity) {
        careDao.insertCareLog(care)
    }
}