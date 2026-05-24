package com.example.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.data.local.AppDatabase
import com.example.petcare.data.repository.PetRepository
import com.example.petcare.ui.screens.HomeScreen
import com.example.petcare.viewmodel.PetViewModel
import com.example.petcare.viewmodel.PetViewModelFactory
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.petcare.ui.theme.PetCareTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // 1. Inicializar DB y Repo (En una app pro esto iría con Inyección de Dependencias)
        val database = AppDatabase.getDatabase(this)
        val repository = PetRepository(database.petDao(), database.careDao())

        setContent {
            // 1. INYECTAMOS EL TEMA
            // Nota: Si el asistente de Android Studio nombró a tu tema diferente (ej. AppTheme),
            // usa ese nombre aquí en lugar de PetCareTheme.
            PetCareTheme {

                // 2. PINTAMOS EL LIENZO BASE
                // Esto garantiza que el fondo detrás de las pantallas sea negro absoluto
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. INICIALIZAMOS VIEWMODEL Y NAVEGACIÓN
                    val petViewModel: PetViewModel = viewModel(
                        factory = PetViewModelFactory(repository)
                    )
                    com.example.petcare.ui.navigation.AppNavigation(viewModel = petViewModel)
                }
            }
        }
    }
}