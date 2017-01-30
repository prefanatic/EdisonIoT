package com.prefanatic.edisoniot

import android.graphics.Color
import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
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
        SocketManager.messageSubject
                .subscribe {
                    when (it.message) {
                        "led-off" -> qsTile.state = STATE_INACTIVE
                        "led-on" -> qsTile.state = STATE_ACTIVE
                    }
                }
    }

    override fun onClick() {
        super.onClick()

        println("Hello World")
        SocketManager.send("/led/", "toggle")
    }
}

fun byteStringColor(@ColorInt color: Int): ByteString {
    val buffer = ByteBuffer.allocate(4)
            .putInt(color)
            .rewind() as ByteBuffer

    return ByteString.of(buffer)
}