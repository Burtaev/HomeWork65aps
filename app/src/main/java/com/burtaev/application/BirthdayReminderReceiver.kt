package com.burtaev.application

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar.getInstance

const val KEY_PERSON_ID = "KEY_PERSON_ID"
const val KEY_TEXT = "KEY_TEXT"
const val KEY_ID = "KEY_ID"
const val BIRTHDAY_CHANNEL_ID = "BIRTHDAY_CHANNEL_ID"
const val KEY_BIRTHDAY = "KEY_BIRTHDAY"

class BirthdayReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra(KEY_TEXT)
        val idContact = intent.getIntExtra(KEY_ID, -1)
        val activityIntent = Intent(context, MainActivity::class.java)
            .putExtra(KEY_PERSON_ID, idContact)
        val pendingIntent =
            PendingIntent.getActivity(context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, BIRTHDAY_CHANNEL_ID)
            .setSmallIcon(R.drawable.default_user_icon)
            .setContentTitle(context.getString(R.string.text_birthday))
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(intent.getIntExtra(KEY_ID, -1), builder.build())
        }
        val dateBirthday = getInstance()
        dateBirthday.timeInMillis = intent.getLongExtra(KEY_BIRTHDAY, 0)
        val millisToRemind = BirthdayReminderNotification().createMillisToRemind(dateBirthday)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            idContact,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(RTC_WAKEUP, millisToRemind, alarmIntent)
    }
}
