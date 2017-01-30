package com.prefanatic.edison.home

import android.graphics.Color
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

/**
 * Created by Cody Goldberg on 12/10/2016.
 */
class RgbDriver(val dataPin: String, val clickPin: String) : AutoCloseable {
    val data: Gpio
    val click: Gpio
    val service = PeripheralManagerService()
    val BUFFER_START = 0 or (0x03 shl 30)

    var color: Int = 0
        get() {
            return field
        }
        set(value) {
            val delta = 255 - Color.alpha(value)

            val time = measureTimeMillis {
                setColor(Math.max(Color.red(value) - delta, 0),
                        Math.max(Color.green(value) - delta, 0),
                        Math.max(Color.blue(value) - delta, 0))
            }
            println("Took $time millis to run")

            field = value
        }

    init {
        data = service.openGpio(dataPin)
        click = service.openGpio(clickPin)

        data.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        click.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        data.setActiveType(Gpio.ACTIVE_HIGH)
        click.setActiveType(Gpio.ACTIVE_HIGH)
    }

    override fun close() {
        data.close()
        click.close()
    }

    public fun send32Zeroes() {
        data.value = false
        for (i in 0..31) {
            click()
        }
    }


    fun setColor(r: Int, g: Int, b: Int) {
        //send32Zeroes()

        var buffer: Int = BUFFER_START

        buffer = buffer or (takeAntiCode(b) shl 28)
        buffer = buffer or (takeAntiCode(g) shl 26)
        buffer = buffer or (takeAntiCode(r) shl 24)


        buffer = buffer or (b shl 16)
        buffer = buffer or (g shl 8)
        buffer = buffer or r

        send(buffer)

        send32Zeroes()
    }

    private fun takeAntiCode(input: Int): Int {
        var output = 0

        if ((input and 0x80) == 0) {
            output = output or 0x02
        }

        if ((input and 0x40) == 0) {
            output = output or 0x01
        }

        return output
    }

    private fun send(out: Int) {
        val compare = 0x80000000.toInt()
        var buffer = out

        for (i in 0..31) {
            data.value = buffer and compare != 0

            buffer = buffer shl 1
            click()
        }
    }

    private fun click() {
        click.value = true
        click.value = false
    }

    private fun microWait(value: Long) {
        val start = System.nanoTime()
        while (System.nanoTime() - start < value) {

        }
    }
}