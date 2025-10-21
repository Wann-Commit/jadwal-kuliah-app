package com.example.jadwalkuliahreminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// ReminderReceiver.kt
class ReminderReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val namaMK = intent.getStringExtra("MATA_KULIAH") ?: ""
        val ruangan = intent.getStringExtra("RUANGAN") ?: ""
        val jam = intent.getStringExtra("JAM") ?: ""

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = androidx.core.app.NotificationCompat.Builder(context, "jadwal_kuliah_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pengingat Kuliah")
            .setContentText("$namaMK dimulai 15 menit lagi di $ruangan (${jam})")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(namaMK.hashCode(), notification)
    }
}