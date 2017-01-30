package com.prefanatic.edisoniot

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ViewUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_color_control.*
import java.nio.ByteBuffer
import java.util.*


/**
 * Created by codygoldberg on 12/26/16.
 */

class MainActivity : AppCompatActivity() {
    val colorAnimator: ValueAnimator = ValueAnimator.ofArgb().apply {
        addUpdateListener {
            val color = it.animatedValue as Int

            window.statusBarColor = scrimify(color, 0.075f)
            window.navigationBarColor = color
            supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_control)

        startService(Intent(this, ConnectionService::class.java))

        color_list.addColor(Color.RED)
        color_list.addColor(Color.GREEN)
        color_list.addColor(Color.BLUE)

        val random = Random()
        for (i in 0..128) {
            color_list.addColor(Color.rgb(random.nextInt(255), random.nextInt(255),
                    random.nextInt(255)))
        }

        color_list.clickSubject.subscribe {
            print("Color $it")
            setColorTheme(it)

            sendColor(it)
        }
    }

    override fun onStart() {
        super.onStart()

        SocketManager.createSocket()
    }

    fun sendColor(@ColorInt color: Int) {
        val buffer = ByteBuffer.allocate(4)
                .putInt(color)
                .rewind() as ByteBuffer

        SocketManager.send(PATH_LED, ACTION_SET_COLOR, buffer)
    }

    fun setColorTheme(@ColorInt color: Int) {
        colorAnimator.apply {
            setIntValues(window.navigationBarColor, color)
            setEvaluator(ArgbEvaluator())
            start()
        }

        if (!isDark(color)) {
            setLightStatusBar(color_list)
        } else {
            clearLightStatusBar(color_list)
        }
    }
}