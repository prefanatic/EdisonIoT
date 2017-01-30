package com.prefanatic.edisoniot

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.View
import kotlinx.android.synthetic.main.activity_color_control.*
import org.jetbrains.anko.doAsync
import java.nio.ByteBuffer


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

        //for (lightness in 1..100) {
            for (hue in 0..360) {
                //for (saturation in 1..100) {
                    color_list.addColor(ColorUtils.HSLToColor(floatArrayOf(hue.toFloat(), 50 / 100f, 50 / 100f)))
                //}
            }
        //}

        fab.setOnClickListener {
            TransitionManager.beginDelayedTransition(coordinator)
            rgb_view.visibility = View.VISIBLE
        }
        rgb_view.setOnClickListener {
            TransitionManager.beginDelayedTransition(coordinator)

            color_list.addColor(rgb_view.color)
            rgb_view.visibility = View.GONE
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

    override fun onBackPressed() {
        if (rgb_view.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(coordinator)
            rgb_view.visibility = View.GONE
            return
        }

        super.onBackPressed()
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