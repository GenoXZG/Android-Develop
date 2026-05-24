package com.example.petcare.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.petcare.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Extraemos los datos que le pasaremos a la alarma
        val petName = intent.getStringExtra("PET_NAME") ?: "tu mascota"
        val careType = intent.getStringExtra("CARE_TYPE") ?: "un cuidado"
        val notificationId = intent.getIntExtra("NOTIF_ID", 1)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // En Android 8.0+ es obligatorio crear un "Canal" de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pet_care_channel",
                "Recordatorios de Mascotas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para alertas de vacunas y comida"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Construimos el diseño de la notificación push
        val notification = NotificationCompat.Builder(context, "pet_care_channel")
            .setSmallIcon(android.R.drawable.ic_popup_reminder) // Ícono por defecto del sistema
            .setContentTitle("¡Aviso de $careType!")
            .setContentText("Es hora de atender a $petName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Disparamos la notificación
        notificationManager.notify(notificationId, notification)
    }
}