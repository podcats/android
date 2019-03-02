package com.example

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.novoda.noplayer.ContentType
import com.novoda.noplayer.OptionsBuilder
import com.novoda.noplayer.PlayerBuilder
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.novoda.noplayer.PlayerType


class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        ContextCompat.startForegroundService(this, Intent(this, PlayerService::class.java))
    }
}

class PlayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null // TODO create binder for player commands or even a bus??!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "player",
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channel.id)
            .setContentTitle("player")
            .setContentText("player")
            .setSmallIcon(R.drawable.ic_launcher)
            .build()
        startForeground(100, notification)

        play("https://dcs.megaphone.fm/HSW5458739817.mp3?key=b05fac12fc24b3ba70beab6e5bef68c3&listener=0193e468-9de6-455c-b033-74f81ee5da94")
    }

    private fun play(url: String) {
        val player = PlayerBuilder()
            .withPriority(PlayerType.MEDIA_PLAYER)
            .build(this)
        val options = OptionsBuilder()
            .withContentType(ContentType.H264)
            .build()

        player.listeners.addPreparedListener {
            Log.e("!!!", "prepared")
            player.play()
        }
      player.listeners.addErrorListener {
        it.detailType()
          Log.e("!!!", "err ${it.message()}")
      }

        player.loadVideo(Uri.parse(url), options)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
}
