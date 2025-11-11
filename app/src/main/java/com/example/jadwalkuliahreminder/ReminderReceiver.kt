package com.example.jadwalkuliahreminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val namaMK = intent.getStringExtra("MATA_KULIAH") ?: ""
        val ruangan = intent.getStringExtra("RUANGAN") ?: ""
        val jam = intent.getStringExtra("JAM") ?: ""
        val reminderType = intent.getStringExtra("REMINDER_TYPE") ?: "sebelum"

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Tentukan judul dan pesan berdasarkan tipe reminder
        val title: String
        val message: String

        when (reminderType) {
            "sebelum" -> {
                title = "Kuliah Akan Dimulai"
                message = "Kuliah akan dimulai 10 menit lagi\n$namaMK di $ruangan ($jam)"
            }
            else -> {
                title = "Kuliah Telah Dimulai"
                message = "Mata kuliah $namaMK telah dimulai\nRuangan: $ruangan ($jam)"
            }
        }

        // Gunakan ringtone default untuk notifikasi
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, "jadwal_kuliah_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000)) // Vibrasi
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (namaMK + reminderType).hashCode()
        notificationManager.notify(notificationId, notification)
    }
}