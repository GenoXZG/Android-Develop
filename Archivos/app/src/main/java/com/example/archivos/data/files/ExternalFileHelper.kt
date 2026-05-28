package com.example.archivos.data.files

import android.content.Context
import android.net.Uri
import java.io.OutputStreamWriter

class ExternalFileHelper(private val context: Context) {

    fun exportarTickets(uri: Uri, contenido: String): Boolean {
        return try {
            
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(contenido)
                }
            }
            true /
        } catch (e: Exception) {
            e.printStackTrace()
            
            false
        }
    }
}
