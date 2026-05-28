package com.example.archivos.data.files

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class InternalFileHelper(private val context: Context) {

    private val FILE_NAME = "borrador_ticket.txt"

    fun guardarBorrador(texto: String) {
        try {

            val fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            val writer = OutputStreamWriter(fileOutputStream)
            writer.write(texto)
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun leerBorrador(): String {
        var textoBorrador = ""
        try {
            val fileInputStream = context.openFileInput(FILE_NAME)
            val reader = BufferedReader(InputStreamReader(fileInputStream))
            val stringBuilder = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
            reader.close()
            textoBorrador = stringBuilder.toString()
        } catch (e: Exception) {

        }
        return textoBorrador.trim()
    }
}