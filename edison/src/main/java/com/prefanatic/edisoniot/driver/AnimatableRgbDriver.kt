package com.prefanatic.edisoniot.driver

import android.animation.ValueAnimator
import android.support.v4.view.animation.FastOutLinearInInterpolator
import com.prefanatic.edison.home.RgbDriver

/**
 * com.prefanatic.edisoniot.driver (Cody Goldberg - 1/12/2017)
 */
class AnimatableRgbDriver(val driver: RgbDriver) {
    val animator: ValueAnimator

    init {
        animator = ValueAnimator.ofArgb(driver.color).apply {
            addUpdateListener {
                driver.color = animatedValue as Int
            }
            duration = 1000L
            //interpolator = FastOutLinearInInterpolator()
        }
    }

    fun setColor(color: Int) {
        animator.setIntValues(driver.color, color)
        animator.start()
    }

    fun getColor(): Int {
        return animator.animatedValue as Int
    }
}