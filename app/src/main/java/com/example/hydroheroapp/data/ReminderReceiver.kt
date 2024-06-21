package com.example.hydroheroapp.data

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.hydroheroapp.R
import com.example.hydroheroapp.view.main.reminder.ReminderFragment

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val time = intent.getStringExtra("time")
        val description = intent.getStringExtra("description")
        val interval = intent.getIntExtra("interval", 5)
        val ringtone = intent.getStringExtra("ringtone")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = time.hashCode()

        val soundEnabled = getSwitchState(context, R.id.switch_sounds)
        val vibrate = getSwitchState(context, R.id.switch_vibrate)

        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Reminder")
            .setContentText(description)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_done, "OK", getActionIntent(context, "OK", notificationId))
            .addAction(R.drawable.ic_snooze, "Remind Me Again", getActionIntent(context, "REMIND_ME_AGAIN", notificationId, interval))

        if (soundEnabled) {
            val ringtoneUri = Uri.parse(ringtone)
            notificationBuilder.setSound(ringtoneUri)
        }
        if (vibrate) {
            notificationBuilder.setVibrate(longArrayOf(0, 1000, 500, 1000))
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun getSwitchState(context: Context, switchId: Int): Boolean {
        val activity = context as? AppCompatActivity ?: return true
        val fragment = activity.supportFragmentManager.findFragmentByTag("ReminderFragment") as? ReminderFragment
            ?: return true
        val switch = fragment.view?.findViewById<Switch>(switchId)
        return switch?.isChecked ?: true
    }

    private fun getActionIntent(context: Context, action: String, notificationId: Int, interval: Int = 0): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            this.action = action
            putExtra("notificationId", notificationId)
            if (interval > 0) putExtra("interval", interval)
        }
        return PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
