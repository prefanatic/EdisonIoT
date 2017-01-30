package com.prefanatic.edisoniot

import android.util.Log
import com.squareup.moshi.Moshi

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by codygoldberg on 12/26/16.
 */

class SocketServer(address: InetSocketAddress) : WebSocketServer(address) {
    private var mSocket: WebSocket? = null

    val messageSubject = PublishSubject.create<Message>()
    val moshi = Moshi.Builder().build()
    val messageAdapter = moshi.adapter(Message::class.java)

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        mSocket = conn
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {

    }

    override fun onMessage(conn: WebSocket, message: String) {
        Log.d(TAG, "onMessage(String): " + message)

        messageSubject.onNext(messageAdapter.fromJson(message))
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        Log.d(TAG, "onMessage(ByteBuffer): " + String(message!!.array()))

        //byteMessageSubject.onNext(message)
    }

    override fun onError(conn: WebSocket, ex: Exception) {
        messageSubject.onError(ex)
    }

    fun asObservable(): Observable<Message> {
        return messageSubject.asObservable()
    }

    companion object {
        private val TAG = "SocketServer"
    }
}