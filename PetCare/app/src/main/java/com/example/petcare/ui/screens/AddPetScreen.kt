package com.example.petcare.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.petcare.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(viewModel: PetViewModel, navController: NavController) {
    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }

    // Estados para el Spinner (Dropdown Menu)
    var expanded by remember { mutableStateOf(false) }
    val petOptions = listOf("Perro", "Gato", "Hurón", "Erizo", "Ave", "Conejo", "Reptil", "Hámster", "Otro")
    var selectedType by remember { mutableStateOf(petOptions[0]) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri?.toString() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Permite scroll si el teclado estorba
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- SECCIÓN SUPERIOR: FOTO GRANDE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // 1. Altura mínima garantizada para el estado vacío, pero con capacidad de crecer
                    .defaultMinSize(minHeight = 200.dp)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Foto seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), // 2. La imagen dicta su propia altura sin restricciones
                        contentScale = ContentScale.Fit // 3. Forzamos a que encaje sin recortes
                    )
                    // Badge flotante para indicar que se puede cambiar
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text("Cambiar", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("Añadir foto de la mascota", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // --- CAMPO: NOMBRE ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Pets, null) },
                singleLine = true
            )

            // --- CAMPO: TIPO (SPINNER / EXPOSED DROPDOWN) ---
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true, // Evita que el usuario escriba manualmente
                    label = { Text("Tipo de Mascota") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    petOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedType = option
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            // --- CAMPO: EDAD ---
            OutlinedTextField(
                value = age,
                onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                label = { Text("Edad (Años)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            if (showError) {
                Text(
                    text = "Completa los campos obligatorios.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- BOTÓN: GUARDAR ---
            Button(
                onClick = {
                    val ageInt = age.toIntOrNull()
                    if (name.isNotBlank() && ageInt != null) {
                        viewModel.addPet(name, selectedType, ageInt, selectedImageUri)
                        navController.popBackStack()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Mascota", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}