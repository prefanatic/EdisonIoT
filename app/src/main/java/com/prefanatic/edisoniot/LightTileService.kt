package com.prefanatic.edisoniot

import android.graphics.Color
import android.service.quicksettings.Tile
import android.service.quicksettings.Tile.*
import android.service.quicksettings.TileService
import android.support.annotation.ColorInt
import okio.ByteString
import java.nio.ByteBuffer

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/14/2017)
 */
class LightTileService : TileService() {
    override fun onCreate() {
        super.onCreate()
        SocketManager.observeMessages()
                .filter { it.path == PATH_LED }
                .subscribe({
                    when (it.message) {
                        "led-off" -> qsTile.state = STATE_INACTIVE
                        "led-on" -> qsTile.state = STATE_ACTIVE
                    }
                }, {
                    print("Error $it")
                })

//        SocketManager.observeState()
//                .doOnCompleted { qsTile.state = STATE_UNAVAILABLE }
//                .subscribe {
//                    qsTile.state = STATE_ACTIVE
//                }
    }

    override fun onClick() {
        super.onClick()

        SocketManager.send(PATH_LED, ACTION_TOGGLE)
    }
}

fun byteStringColor(@ColorInt color: Int): ByteString {
    val buffer = ByteBuffer.allocate(4)
            .putInt(color)
            .rewind() as ByteBuffer

    return ByteString.of(buffer)
}