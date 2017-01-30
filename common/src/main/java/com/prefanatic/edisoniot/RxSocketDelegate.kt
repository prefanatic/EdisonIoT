package com.prefanatic.edisoniot

import com.squareup.moshi.Moshi
import rx.subjects.PublishSubject

/**
 * Created by cody on 1/30/17.
 */
class RxSocketDelegate<Socket> {
    val messageSubject = PublishSubject.create<Message>()
    val stateSubject = PublishSubject.create<Socket>()

    private val moshi = Moshi.Builder().build()
    private val messageAdapter = moshi.adapter(Message::class.java)

    fun onOpened(socket: Socket) {
        print("Opened")
        stateSubject.onNext(socket)
    }

    fun onClosed() {
        print("Closed")
        stateSubject.onCompleted()
    }

    fun onMessage(message: String) {
        print("Message")
        messageSubject.onNext(messageAdapter.fromJson(message))
    }

    fun onError(throwable: Throwable) {
        print("Error")
        messageSubject.onError(throwable)
        stateSubject.onError(throwable)
    }

    fun buildMessage(message: Message): String = messageAdapter.toJson(message)
}