package com.prefanatic.edisoniot

import com.squareup.moshi.Moshi
import okhttp3.*
import okio.ByteString
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import java.nio.ByteBuffer

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/14/2017)
 */
object SocketManager {
    val client = OkHttpClient()
    val stateSubject = BehaviorSubject.create<WebSocket>()
    val messageSubject = PublishSubject.create<Message>()

    val moshi = Moshi.Builder().build()
    val messageAdapter = moshi.adapter(Message::class.java)

    val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket?, response: Response?) {
            print("Opened.")

            stateSubject.onNext(webSocket)
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            t?.printStackTrace()
            print("Failed.")

            stateSubject.onError(t)
            socket = null
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            print("Closing.")

            stateSubject.onCompleted()
            socket = null
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            print("Message: $text")

            messageSubject.onNext(messageAdapter.fromJson(text))
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            print("Message (b): $bytes")
        }

        override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
            print("Closed.")

            socket = null
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
        return messageSubject.asObservable()
    }

    fun send(path: String, message: String, data: ByteBuffer? = null) {
        val message = Message(path, message, data?.array())

        socket?.send(messageAdapter.toJson(message))
    }
}