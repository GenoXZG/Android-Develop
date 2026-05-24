package com.example.petcare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.petcare.ui.components.PetItem
import com.example.petcare.viewmodel.PetViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PetViewModel, navController: NavController) {
    val pets by viewModel.pets.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Mascotas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_pet") // <-- Redirige a la ruta del formulario
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(pets, key = { it.id }) { pet ->
                val state = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            viewModel.deletePet(pet)
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = state,
                    backgroundContent = {
                        val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.White)
                        }
                    },
                    content = {
                        PetItem(pet = pet, onClick = { navController.navigate("pet_detail/${pet.id}") })
                    }
                )
            }
        }
    }
}