package com.sudzusama.vkimageclassifier.utils.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import androidx.annotation.NonNull
import java.lang.ref.WeakReference

class ChipSpan(dr: Drawable) : ImageSpan(dr) {
    private var mDrawableRef: WeakReference<Drawable>? = null

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        cachedDrawable?.bounds?.let { rect ->
            fm?.let {
                val pfm = paint.fontMetricsInt
                val height = rect.height() + 8.toPx
                it.ascent = -height / 2 + pfm.ascent / 2
                it.descent = 0.coerceAtLeast(height / 2 + pfm.ascent / 2)
                it.top = it.ascent
                it.bottom = it.descent
            }
            return rect.right
        } ?: return super.getSize(paint, text, start, end, fm)
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        @NonNull paint: Paint
    ) {
        val b = cachedDrawable!!
        canvas.save()
        val transY = (bottom + top) / 2 - b.bounds.height() / 2
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    // Redefined locally because it is a private member from DynamicDrawableSpan
    private val cachedDrawable: Drawable?
        get() = mDrawableRef?.get() ?: drawable.also { mDrawableRef = WeakReference(it) }

}
