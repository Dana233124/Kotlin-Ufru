package com.example.urfu.profile.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.urfu.MainActivity
import com.example.urfu.R

class PairReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("DEBUG", "PairReceiver TRIGGERED!")

        val name = intent.getStringExtra("name") ?: "Студент"
        Log.d("DEBUG", "PairReceiver got name=$name")

        val channelId = "pair_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Пары",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
            Log.d("DEBUG", "Notification channel created/exists")
        }

        val openIntent = Intent(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Начинается пара!")
            .setContentText("$name, пора на любимую пару")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        Log.d("DEBUG", "Showing notification now")
        manager.notify(1, notification)
    }
}
