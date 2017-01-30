package com.prefanatic.edisoniot.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.StringRes
import android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Title
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.prefanatic.edisoniot.R
import com.prefanatic.edisoniot.convertDpToPixel


/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/29/2017)
 */

class RgbSelectView : LinearLayout {
    lateinit var red: SeekBar
    lateinit var green: SeekBar
    lateinit var blue: SeekBar

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

        val redLabel = label(R.string.color_red)
        val greenLabel = label(R.string.color_green)
        val blueLabel = label(R.string.color_blue)

        red = SeekBar(context).apply {
            max = 255
        }

        green = SeekBar(context).apply {
            max = 255
        }

        blue = SeekBar(context).apply {
            max = 255
        }

        val params = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .apply {
                    bottomMargin = convertDpToPixel(16f, context).toInt()
                }

        val labelParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        addView(redLabel, labelParams)
        addView(red, params)

        addView(greenLabel, labelParams)
        addView(green, params)

        addView(blueLabel, labelParams)
        addView(blue, params)
    }

    fun label(@StringRes stringRes: Int) : TextView {
        val label = TextView(context).apply {
            text = context.getString(stringRes)
            setTextAppearance(TextAppearance_AppCompat_Title)

            val padding = convertDpToPixel(16f, context).toInt()
            setPadding(padding, padding, padding, padding)
        }

        return label
    }

}
