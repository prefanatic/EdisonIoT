package com.prefanatic.edisoniot

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.PRIORITY_MIN
import android.support.v4.app.NotificationManagerCompat

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/14/2017)
 */
class ConnectionService : Service() {
    val ACTION_CONNECT = "action.connect"

    override fun onCreate() {
        super.onCreate()

        handleSocket()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> SocketManager.createSocket()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun handleSocket() {
        SocketManager.createSocket()
        SocketManager.observeState()
                .subscribe({ socket ->
                    buildNotification()

                    print("$socket is open.")
                }, { error ->
                    print("Socket error.")

                    buildNotification()
                    //SocketManager.createSocket()
                }, {
                    print("Socket has completed.")

                    buildNotification()
                    //SocketManager.createSocket()
                })
    }

    fun buildNotification() {
        val connected = SocketManager.socket != null
        val contentText = when (connected) {
            true -> "Connected to Edison"
            false -> "Disconnected"
        }
        val contentTitle = "EdisonIoT"

        val builder = NotificationCompat.Builder(this).apply {
            mContentText = contentText
            mContentTitle = contentTitle
            setSmallIcon(R.mipmap.ic_launcher)
            priority = PRIORITY_MIN

            if (!connected) {
                val intent = Intent(this@ConnectionService, ConnectionService::class.java).apply {
                    action = ACTION_CONNECT
                }

                val pending = PendingIntent.getService(this@ConnectionService, 1, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

                addAction(R.mipmap.ic_launcher, "Connect", pending)
            }
        }

        NotificationManagerCompat.from(this)
                .notify(100, builder.build())
    }

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}