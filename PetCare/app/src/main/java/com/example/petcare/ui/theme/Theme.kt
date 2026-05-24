package com.example.petcare.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Paleta estricta monocromática y minimalista
private val MonochromeDarkScheme = darkColorScheme(
    primary = Color.White,                  // Acentos y botones principales
    onPrimary = Color.Black,                // Texto sobre botones principales
    primaryContainer = Color(0xFF222222),   // Gris oscuro para etiquetas (como la del nombre)
    onPrimaryContainer = Color.White,       // Texto sobre etiquetas
    secondary = Color.LightGray,            // Elementos secundarios (iconos inactivos)
    background = Color.Black,               // Fondo absoluto de la app
    surface = Color(0xFF121212),            // Fondo de tarjetas base (ligeramente elevado)
    surfaceVariant = Color(0xFF1E1E1E),     // Fondo de tarjetas destacadas y modales
    onSurface = Color.White,                // Texto principal
    onSurfaceVariant = Color(0xFFCCCCCC),   // Texto secundario (gris claro)
    error = Color(0xFFCF6679)               // El rojo de error se mantiene por normas de UX/Accesibilidad
)

@Composable
fun PetCareTheme(
    content: @Composable () -> Unit
) {
    // Forzamos el esquema monocromático ignorando las preferencias del sistema
    val colorScheme = MonochromeDarkScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Forzamos la barra de estado (donde está la hora y batería) a negro absoluto
            window.statusBarColor = Color.Black.toArgb()
            // Forzamos los íconos de la barra de estado a color claro
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asume que tienes tu archivo Typography.kt intacto
        content = content
    )
}