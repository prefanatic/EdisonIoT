package com.prefanatic.edisoniot

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.util.DisplayMetrics
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.os.Build
import android.support.annotation.NonNull
import android.view.View


/**
 * com.prefanatic.edisoniot (Cody Goldberg - 1/29/2017)
 */

/**
 * This method converts dp unit to equivalent pixels, depending on device density.

 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun convertDpToPixel(dp: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px
}

/**
 * This method converts device specific pixels to density independent pixels.

 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun convertPixelsToDp(px: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp
}

/**
 * Calculate a variant of the color to make it more suitable for overlaying information. Light
 * colors will be lightened and dark colors will be darkened

 * @param color the color to adjust
 * @param isDark whether `color` is light or dark
 * @param lightnessMultiplier the amount to modify the color e.g. 0.1f will alter it by 10%
 * @return the adjusted color
 */
@ColorInt fun scrimify(@ColorInt color: Int,
                       isDark: Boolean,
                       @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float): Int {
    var lightnessMultiplier = lightnessMultiplier
    val hsl = FloatArray(3)
    android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl)

    if (!isDark) {
        lightnessMultiplier += 1f
    } else {
        lightnessMultiplier = 1f - lightnessMultiplier
    }

    hsl[2] = constrain(0f, 1f, hsl[2] * lightnessMultiplier)
    return android.support.v4.graphics.ColorUtils.HSLToColor(hsl)
}

@ColorInt fun scrimify(@ColorInt color: Int,
                       @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float): Int {
    return scrimify(color, isDark(color), lightnessMultiplier)
}

/**
 * Check that the lightness value (0–1)
 */
fun isDark(hsl: FloatArray): Boolean { // @Size(3)
    return hsl[2] < 0.5f
}

/**
 * Convert to HSL & check that the lightness value
 */
fun isDark(@ColorInt color: Int): Boolean {
    val hsl = FloatArray(3)
    android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl)
    return isDark(hsl)
}

fun constrain(min: Float, max: Float, v: Float): Float {
    return Math.max(min, Math.min(max, v))
}

fun setLightStatusBar(view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}

fun clearLightStatusBar(view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = view.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        view.systemUiVisibility = flags
    }
}