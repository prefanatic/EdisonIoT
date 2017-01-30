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
    private val delegate = RxSocketDelegate<WebSocket>()

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        mSocket = conn

        delegate.onOpened(conn)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        delegate.onClosed()
    }

    override fun onMessage(conn: WebSocket, message: String) {
        Log.d(TAG, "onMessage(String): " + message)

        delegate.onMessage(message)
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        Log.d(TAG, "onMessage(ByteBuffer): " + String(message!!.array()))

        //byteMessageSubject.onNext(message)
    }

    override fun onError(conn: WebSocket, ex: Exception) {
        delegate.onError(ex)
    }

    fun asObservable(): Observable<Message> {
        return delegate.messageSubject.asObservable()
    }

    companion object {
        private val TAG = "SocketServer"
    }
}