package com.example.archivos.data.sqlite

import android.content.Context

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class TicketDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Al subir la versión de la base de datos, destruimos la anterior y la recreamos.
        // ADVERTENCIA CRÍTICA: En un entorno de producción, esto borra todos los datos del usuario.
        // Aquí se deberían programar scripts de migración (ALTER TABLE), pero para este alcance es suficiente.
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        // Si modificas las columnas en el Contract en el futuro, DEBES cambiar la versión a 2.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Bitacora.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TicketContract.TicketEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${TicketContract.TicketEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${TicketContract.TicketEntry.COLUMN_NAME_DESCRIPTION} TEXT)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${TicketContract.TicketEntry.TABLE_NAME}"
    }
}