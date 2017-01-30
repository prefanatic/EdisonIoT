package com.prefanatic.edisoniot

import java.nio.ByteBuffer

/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/11/2017)
 */
data class Message(val path: String, val message: String,
                   val data: ByteArray? = null)