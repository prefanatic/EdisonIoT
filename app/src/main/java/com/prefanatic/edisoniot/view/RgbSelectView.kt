package com.prefanatic.edisoniot.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.StringRes
import android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Title
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.prefanatic.edisoniot.R
import com.prefanatic.edisoniot.convertDpToPixel
import org.jetbrains.anko.onSeekBarChangeListener
import org.jetbrains.anko.seekBar


/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/29/2017)
 */

class RgbSelectView : LinearLayout {
    lateinit var red: SeekBar
    lateinit var green: SeekBar
    lateinit var blue: SeekBar

    lateinit var submit: Button

    val onSeekbarChangedListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            submit.setTextColor(color)
        }
    }

    var color: Int
        get() = Color.rgb(red.progress, green.progress, blue.progress)
        set(value) {
            red.progress = Color.red(value)
            green.progress = Color.green(value)
            blue.progress = Color.blue(value)
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        orientation = VERTICAL
        setBackgroundColor(Color.WHITE)

        val redLabel = label(R.string.color_red)
        val greenLabel = label(R.string.color_green)
        val blueLabel = label(R.string.color_blue)

        val labelList = listOf(redLabel, greenLabel, blueLabel)

        submit = AppCompatButton(context).apply {
            text = "Apply"
            background = null
        }

        red = AppCompatSeekBar(context)
        green = AppCompatSeekBar(context)
        blue = AppCompatSeekBar(context)


        val params = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .apply {
                    bottomMargin = convertDpToPixel(16f, context).toInt()
                }

        val labelParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        listOf(red, green, blue).forEachIndexed { i, seekBar ->
            seekBar.apply {
                max = 255
                setOnSeekBarChangeListener(onSeekbarChangedListener)
            }

            addView(labelList[i], labelParams)
            addView(seekBar, params)
        }

        addView(submit, labelParams)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        submit.setOnClickListener(l)
    }

    fun label(@StringRes stringRes: Int): TextView {
        val label = TextView(context).apply {
            text = context.getString(stringRes)
            setTextAppearance(TextAppearance_AppCompat_Title)

            val padding = convertDpToPixel(16f, context).toInt()
            setPadding(padding, padding, padding, padding)
        }

        return label
    }

}
