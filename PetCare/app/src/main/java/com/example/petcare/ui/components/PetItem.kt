package com.example.petcare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petcare.data.local.entities.PetEntity

@Composable
fun PetItem(pet: PetEntity, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Definimos un radio de esquina estándar para ambos casos
            val petImageShape = RoundedCornerShape(8.dp)

            // LÓGICA DE CARGA MULTIMEDIA CON COIL (REFINADA)
            if (pet.imageUri != null) {
                AsyncImage(
                    model = pet.imageUri,
                    contentDescription = "Foto de ${pet.name}",
                    modifier = Modifier
                        .size(50.dp) // Mantenemos el tamaño
                        .clip(petImageShape), // CAMBIO: Cuadrado redondeado
                    contentScale = ContentScale.Crop // Mantiene el aspecto sin estirar
                )
            } else {
                // Respaldo por defecto estilizado (Contenedor cuadrado redondeado)
                Box(
                    modifier = Modifier
                        .size(50.dp) // Mismo tamaño que la imagen
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant, // Color de fondo gris suave
                            petImageShape // Misma forma redondeada
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp), // Ícono centrado ligeramente más pequeño
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = pet.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = "${pet.type} • ${pet.age} años", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}