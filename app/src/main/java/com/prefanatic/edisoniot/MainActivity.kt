package com.prefanatic.edisoniot

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.prefanatic.edisoniot.SocketManager.socket
import kotlinx.android.synthetic.main.activity_color_control.*
import okio.ByteString
import java.nio.ByteBuffer

/**
 * Created by codygoldberg on 12/26/16.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_control)

        startService(Intent(this, ConnectionService::class.java))

        send_button.setOnClickListener {
            val buffer = ByteBuffer.allocate(4)
                    .putInt(rgb_view.color)
                    .rewind() as ByteBuffer

            SocketManager.send("/led/", "set.color", buffer)
        }
    }

    override fun onStart() {
        super.onStart()

        SocketManager.createSocket()
    }
}

fun print(msg: String) {
    Log.d("Tag", msg)
}