package com.example.archivos.screen
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.archivos.data.files.ExternalFileHelper
import com.example.archivos.data.files.InternalFileHelper
import com.example.archivos.data.sqlite.TicketContract
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.archivos.data.sqlite.TicketDbHelper
import android.provider.BaseColumns
import com.example.archivos.data.models.Ticket

// ALUMNO: Zarate Gonzalez Luis David
// Materia: Programacion Movil 1

class MainActivity : ComponentActivity() {

    
    private lateinit var dbHelper: TicketDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = TicketDbHelper(applicationContext)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BitacoraScreen(dbHelper)
                }
            }
        }
    }

    override fun onDestroy() {
        dbHelper.close() 
        super.onDestroy()
    }
}

fun obtenerTicketsDeBD(dbHelper: TicketDbHelper): List<Ticket> {
    val db = dbHelper.readableDatabase
    val projection = arrayOf(
        BaseColumns._ID,
        TicketContract.TicketEntry.COLUMN_NAME_TITLE,
        TicketContract.TicketEntry.COLUMN_NAME_DESCRIPTION
    )

    val cursor = db.query(
        TicketContract.TicketEntry.TABLE_NAME,
        projection, null, null, null, null, "${BaseColumns._ID} DESC" 
    )

    val listaTickets = mutableListOf<Ticket>()
    with(cursor) {
        while (moveToNext()) {
            val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
            val titulo = getString(getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_NAME_TITLE))
            val descripcion = getString(getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_NAME_DESCRIPTION))
            listaTickets.add(Ticket(id, titulo, descripcion))
        }
    }
    cursor.close()
    return listaTickets
}

@Composable
fun BitacoraScreen(dbHelper: TicketDbHelper) {
    val context = LocalContext.current
    val internalHelper = remember { InternalFileHelper(context) }
    val externalHelper = remember { ExternalFileHelper(context) }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    
    var listaTickets by remember { mutableStateOf(obtenerTicketsDeBD(dbHelper)) }

    LaunchedEffect(Unit) {
        descripcion = internalHelper.leerBorrador()
    }

    val exportarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let {
            
            val historial = listaTickets.joinToString(separator = "\n\n") { ticket ->
                "ID: ${ticket.id} | Título: ${ticket.titulo}\nDescripción: ${ticket.descripcion}"
            }
            val exito = externalHelper.exportarTickets(it, historial)
            val mensaje = if (exito) "Exportado con éxito" else "Error al exportar"
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del Ticket") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
                internalHelper.guardarBorrador(it)
            },
            label = { Text("Descripción (Borrador automático)") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            maxLines = 5
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (titulo.isBlank() || descripcion.isBlank()) {
                    Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put(TicketContract.TicketEntry.COLUMN_NAME_TITLE, titulo)
                    put(TicketContract.TicketEntry.COLUMN_NAME_DESCRIPTION, descripcion)
                }

                val newRowId = db.insert(TicketContract.TicketEntry.TABLE_NAME, null, values)
                if (newRowId != -1L) {
                    Toast.makeText(context, "Guardado en SQLite", Toast.LENGTH_SHORT).show()
                    titulo = ""
                    descripcion = ""
                    internalHelper.guardarBorrador("")

                    
                    listaTickets = obtenerTicketsDeBD(dbHelper)
                }
            }) {
                Text("Guardar Ticket")
            }

            Button(onClick = {
                exportarLauncher.launch("Historial_Tickets_${System.currentTimeMillis()}.txt")
            }) {
                Text("Exportar Logs")
            }
        }

        Divider()
        Text("Historial de Tickets", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaTickets) { ticket ->
                TicketItem(ticket)
            }
        }
    }
}

@Composable
fun TicketItem(ticket: Ticket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "#${ticket.id} - ${ticket.titulo}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = ticket.descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
