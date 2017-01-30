package com.prefanatic.edisoniot.extension

import android.support.annotation.ColorInt
import okio.ByteString
import java.nio.ByteBuffer

/**
 * com.prefanatic.edisoniot.extension (Cody Goldberg - 1/14/2017)
 */

fun ByteString.color(@ColorInt color: Int) : ByteString {
    val buffer = ByteBuffer.allocate(4)
            .putInt(color)
            .rewind() as ByteBuffer

    return ByteString.of(buffer)
}