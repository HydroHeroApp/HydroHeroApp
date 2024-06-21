package com.example.hydroheroapp.view.main.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import java.util.*

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val action = intent.action
        val notificationManager = NotificationManagerCompat.from(context)

        when (action) {
            "OK" -> {
                notificationManager.cancel(notificationId)
                Toast.makeText(context, "Reminder dismissed", Toast.LENGTH_SHORT).show()
            }
            "REMIND_ME_AGAIN" -> {
                val interval = intent.getIntExtra("interval", 5)
                val ringtone = intent.getStringExtra("ringtone") ?: "default"
                notificationManager.cancel(notificationId)
                scheduleNextReminder(context, interval, notificationId, ringtone)
                Toast.makeText(context, "Reminder snoozed for $interval minutes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNextReminder(context: Context, interval: Int, notificationId: Int, ringtone: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderIntent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("time", Calendar.getInstance().time.toString())
            putExtra("description", "Reminder description")
            putExtra("interval", interval)
            putExtra("soundEnabled", true)
            putExtra("ringtone", ringtone)
            putExtra("vibrate", true)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, interval)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
