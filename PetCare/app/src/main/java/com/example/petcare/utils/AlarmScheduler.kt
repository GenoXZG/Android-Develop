package com.example.petcare.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleReminder(context: Context, petName: String, careType: String, delayInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // El intent que se le entregará a nuestro ReminderReceiver
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("PET_NAME", petName)
            putExtra("CARE_TYPE", careType)
            putExtra("NOTIF_ID", System.currentTimeMillis().toInt()) // ID único para que no se sobreescriban
        }

        // PendingIntent es un permiso para que AlarmManager ejecute nuestro intent en el futuro
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            intent.getIntExtra("NOTIF_ID", 0),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculamos el tiempo exacto en el futuro
        val triggerTime = System.currentTimeMillis() + delayInMillis

        // Programamos la alarma exacta
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}