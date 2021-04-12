package com.burtaev.application

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.GregorianCalendar

class BirthdayReminderNotification {
    fun setBirthdayReminderEnabled(context: Context, contact: Contact, enabled: Boolean) {
        contact.dateBirthday ?: return
        val intent = Intent(context, BirthdayReminderReceiver::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (enabled) {
            intent.putExtra(KEY_ID, contact.id)
                .putExtra(KEY_BIRTHDAY, contact.dateBirthday.timeInMillis)
                .putExtra(
                    KEY_TEXT,
                    String.format(context.getString(R.string.text_remind_birthday), contact.name)
                )

            val alarmIntent = PendingIntent.getBroadcast(
                context,
                contact.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val millisToRemind = createMillisToRemind(contact.dateBirthday)
            alarmManager.set(AlarmManager.RTC_WAKEUP, millisToRemind, alarmIntent)
        } else {
            val alarmIntent = PendingIntent.getBroadcast(
                context,
                contact.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(alarmIntent)
            alarmIntent.cancel()
        }
    }

    fun checkBirthdayReminder(context: Context, contact: Contact) = PendingIntent.getBroadcast(
        context, contact.id,
        Intent(context, BirthdayReminderReceiver::class.java),
        PendingIntent.FLAG_NO_CREATE
    ) != null

    fun createMillisToRemind(dateBirthday: Calendar) = GregorianCalendar().apply {
        val currentYear = get(Calendar.YEAR)
        set(Calendar.YEAR, dateBirthday.get(Calendar.YEAR))
        set(Calendar.MONTH, dateBirthday.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, dateBirthday.get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 12)
        if (get(Calendar.MONTH) == Calendar.FEBRUARY && get(Calendar.DAY_OF_MONTH) == 29 &&
            !isLeapYear(currentYear)
        ) {
            set(Calendar.DAY_OF_MONTH, 28)
        }
        set(Calendar.YEAR, currentYear)
        if (timeInMillis < System.currentTimeMillis()) {
            add(Calendar.YEAR, 1)
        }
    }.timeInMillis
}