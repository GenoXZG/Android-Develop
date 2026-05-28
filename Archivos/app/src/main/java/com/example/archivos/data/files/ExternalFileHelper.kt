package com.example.archivos.data.files

import android.content.Context
import android.net.Uri
import java.io.OutputStreamWriter

class ExternalFileHelper(private val context: Context) {

    /**
     * Exporta el historial completo de tickets a un archivo en el almacenamiento externo.
     * @param uri La ruta segura generada por el Storage Access Framework.
     * @param contenido El texto estructurado con todos los tickets.
     */
    fun exportarTickets(uri: Uri, contenido: String): Boolean {
        return try {
            // El ContentResolver es el puente seguro para acceder a los archivos del sistema
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(contenido)
                }
            }
            true // Retornamos true si la exportación fue exitosa
        } catch (e: Exception) {
            e.printStackTrace()
            // Área de mejora: Manejar el fallo y notificar a la capa superior (UI)
            false
        }
    }
}