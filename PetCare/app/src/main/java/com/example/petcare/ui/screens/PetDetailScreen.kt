package com.example.petcare.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight // IMPORTADO PARA EL ESTILO DEL NOMBRE
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.petcare.data.local.entities.PetEntity
import com.example.petcare.utils.AlarmScheduler
import com.example.petcare.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(petId: Int, viewModel: PetViewModel, navController: NavController) {
    val context = LocalContext.current
    var pet by remember { mutableStateOf<PetEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }
    val dateTimeFormatter = remember { SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()) }

    var selectedDateTimeText by remember { mutableStateOf("No se ha seleccionado fecha/hora") }
    var finalTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val careLogs by viewModel.getCareLogs(petId).collectAsState(initial = emptyList())

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    LaunchedEffect(petId) {
        pet = viewModel.getPetById(petId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // REFINAMIENTO: Ahora muestra el contexto de la pantalla, no el nombre dinámico
                title = { Text("Detalles de la Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                finalTimestamp = System.currentTimeMillis()
                selectedDateTimeText = dateTimeFormatter.format(Date(finalTimestamp))
                showAddDialog = true
            }) {
                Icon(Icons.Default.Event, contentDescription = "Agendar Cuidado")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // --- ITEM 1: SECCIÓN DEL NOMBRE Y FOTO REDONDEADA ---
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally // <-- Centra todo el contenido de la columna
                ) {
                    // ETIQUETA DE COLOR PARA EL NOMBRE
                    Surface(
                        shape = RoundedCornerShape(24.dp), // Forma de píldora
                        color = MaterialTheme.colorScheme.primaryContainer, // Color dinámico de énfasis
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = pet?.name ?: "Cargando...",
                            style = MaterialTheme.typography.headlineMedium, // Tamaño ajustado para la etiqueta
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer, // Contraste automático
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp) // Espaciado interno de la etiqueta
                        )
                    }

                    // CONTENEDOR DE LA IMAGEN CON BORDES REDONDEADOS
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pet?.imageUri != null) {
                            AsyncImage(
                                model = pet?.imageUri,
                                contentDescription = "Foto de ${pet?.name}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pets,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sin foto de perfil",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // --- ITEM 2: DETALLES DE LA MASCOTA ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Especie", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(pet?.type ?: "---", style = MaterialTheme.typography.titleMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Edad", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text("${pet?.age ?: "-"} años", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            // --- ITEM 3: TÍTULO DE LA SECCIÓN ---
            item {
                Text(
                    text = "Historial de Cuidados",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // --- RENDERIZADO DEL HISTORIAL RELACIONAL ---
            if (careLogs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay registros. Agenda uno con el botón flotante.")
                    }
                }
            } else {
                items(careLogs) { log ->
                    ListItem(
                        headlineContent = { Text(log.careType) },
                        supportingContent = {
                            val dateStr = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(log.dateTimestamp))
                            Text(dateStr)
                        },
                        trailingContent = {
                            if (log.dateTimestamp > System.currentTimeMillis()) {
                                Icon(Icons.Default.NotificationImportant, contentDescription = "Agendado", tint = MaterialTheme.colorScheme.secondary)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Completado", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }

        // --- DIÁLOGOS ---
        if (showAddDialog) {
            var careName by remember { mutableStateOf("") }
            var setAlarm by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Agendar Cuidado") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = careName,
                            onValueChange = { careName = it },
                            label = { Text("¿Qué cuidado es? (ej. Vacuna)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { showDatePicker = true }, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.CalendarToday, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Fecha")
                            }
                            Button(onClick = { showTimePicker = true }, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.AccessTime, null)
                                Spacer(Modifier.width(4.dp))
                                Text("Hora")
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Programado para:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                Text(selectedDateTimeText, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("¿Activar recordatorio push?", style = MaterialTheme.typography.bodyMedium)
                            Switch(checked = setAlarm, onCheckedChange = { setAlarm = it })
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (careName.isNotBlank()) {
                                viewModel.addCareLog(petId = petId, careType = careName, timestamp = finalTimestamp)
                                if (setAlarm && finalTimestamp > System.currentTimeMillis()) {
                                    AlarmScheduler.scheduleReminder(context, pet?.name ?: "Mascota", careName, finalTimestamp - System.currentTimeMillis())
                                }
                                showAddDialog = false
                            }
                        }
                    ) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
                }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            utcCalendar.timeInMillis = selectedMillis
                            calendar.set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR))
                            calendar.set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH))
                            calendar.set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH))
                            finalTimestamp = calendar.timeInMillis
                            selectedDateTimeText = dateTimeFormatter.format(Date(finalTimestamp))
                        }
                        showDatePicker = false
                    }) { Text("Confirmar") }
                }
            ) { DatePicker(state = datePickerState) }
        }

        if (showTimePicker) {
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        calendar.set(Calendar.MINUTE, timePickerState.minute)
                        finalTimestamp = calendar.timeInMillis
                        selectedDateTimeText = dateTimeFormatter.format(Date(finalTimestamp))
                        showTimePicker = false
                    }) { Text("Confirmar") }
                },
                text = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TimePicker(state = timePickerState)
                    }
                }
            )
        }
    }
}