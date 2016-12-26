package com.prefanatic.edisoniot

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import okhttp3.*
import okio.ByteString

/**
 * Created by codygoldberg on 12/26/16.
 */

class MainActivity : AppCompatActivity() {

    lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        client = OkHttpClient()

        val request = Request.Builder()
                .url("ws://192.168.1.117:12345")
                .build()

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket?, response: Response?) {
                print("Opened.")
            }

            override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
                t?.printStackTrace()
                print("Failed.")
            }

            override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                print("Closing.")
            }

            override fun onMessage(webSocket: WebSocket?, text: String?) {
                print("Message: $text")
            }

            override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
                print("Message (b): $bytes")
            }

            override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
                print("Closed.")
            }
        })
    }
}

fun print(msg: String) {
    Log.d("Tag", msg)
}