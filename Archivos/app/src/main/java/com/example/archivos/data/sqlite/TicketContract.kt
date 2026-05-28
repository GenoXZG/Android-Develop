package com.example.archivos.data.sqlite

import android.provider.BaseColumns

object TicketContract {
    object TicketEntry : BaseColumns {
        const val TABLE_NAME = "tickets"
        const val COLUMN_NAME_TITLE = "titulo"
        const val COLUMN_NAME_DESCRIPTION = "descripcion"
    }
}
