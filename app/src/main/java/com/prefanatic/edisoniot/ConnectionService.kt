package com.prefanatic.edisoniot

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/14/2017)
 */
class ConnectionService : Service() {

    override fun onCreate() {
        super.onCreate()

        SocketManager.createSocket()
        SocketManager.stateSubject
                .subscribe({ socket ->
                    print("$socket is open.")
                }, { error ->
                    print("Socket error.")

                    //SocketManager.createSocket()
                }, {
                    print("Socket has completed.")

                    //SocketManager.createSocket()
                })
    }

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}