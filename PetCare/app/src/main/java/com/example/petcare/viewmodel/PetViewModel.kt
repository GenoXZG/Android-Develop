package com.example.petcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entities.PetEntity
import com.example.petcare.data.local.entities.CareEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    // 1. Estado reactivo: Convertimos el Flow del repositorio en un StateFlow
    // que sobrevive a cambios de configuración (como girar la pantalla).
    val pets: StateFlow<List<PetEntity>> = repository.allPets
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Optimiza memoria si la app está en segundo plano
            initialValue = emptyList()
        )

    // 2. Acción: Agregar Mascota
    fun addPet(name: String, type: String, age: Int, imageUri: String?) {
        viewModelScope.launch {
            val newPet = PetEntity(
                name = name,
                type = type,
                age = age,
                imageUri = imageUri // <--- Se vincula la foto al registro
            )
            repository.insertPet(newPet)
        }
    }

    // 3. Acción: Eliminar Mascota
    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }

    // ==========================================
    // OPERACIONES DE CUIDADOS (HISTORIAL)
    // ==========================================

    // Recupera una mascota específica para el encabezado (transaccional)
    suspend fun getPetById(petId: Int): PetEntity? {
        return repository.getPetById(petId)
    }

    // Retorna el flujo reactivo de los cuidados de una sola mascota
    fun getCareLogs(petId: Int): kotlinx.coroutines.flow.Flow<List<CareEntity>> {
        return repository.getCareLogsForPet(petId)
    }

    // Inserta un nuevo registro de cuidado con la fecha exacta del sistema
    fun addCareLog(petId: Int, careType: String,timestamp: Long, notes: String = "") {
        viewModelScope.launch {
            val newCare = CareEntity(
                petId = petId,
                careType = careType,
                dateTimestamp = timestamp, // Unix timestamp actual
                notes = notes.takeIf { it.isNotBlank() }
            )
            repository.insertCareLog(newCare)
        }
    }
}

// 4. Fábrica necesaria porque el ViewModel recibe un Repositorio por parámetro
class PetViewModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}
