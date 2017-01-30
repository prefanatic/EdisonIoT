package com.prefanatic.edisoniot

import okhttp3.*
import okio.ByteString
import rx.Observable
import java.nio.ByteBuffer

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/14/2017)
 */
object SocketManager {
    val client = OkHttpClient()

    private val delegate = RxSocketDelegate<WebSocket>()

    val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            print("Opened.")

            delegate.onOpened(webSocket!!)
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            t?.printStackTrace()
            print("Failed.")

            socket = null
            delegate.onError(t!!)
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            print("Closing.")

            socket = null
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            print("Message: $text")

            delegate.onMessage(text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            print("Message (b): $bytes")
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
            print("Closed.")

            socket = null
            delegate.onClosed()
        }
    }

    var socket: WebSocket? = null

    init {

    }

    fun createSocket() {
        if (socket != null)
            return

        val request = Request.Builder()
                .url("ws://10.0.0.226:12345")
                .build()

        socket = client.newWebSocket(request, socketListener)
    }

    fun observeMessages(): Observable<Message> {
        return delegate.messageSubject.asObservable()
    }

    fun observeState(): Observable<WebSocket> {
        return delegate.stateSubject.asObservable()
    }

    fun send(path: String, message: String, data: ByteBuffer? = null) {
        val message = Message(path, message, data?.array())

        socket?.send(delegate.buildMessage(message))
    }
}