package com.sudzusama.vkimageclassifier.utils.view.dominantcolor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import com.sudzusama.vkimageclassifier.R
import com.sudzusama.vkimageclassifier.utils.view.invisible
import com.sudzusama.vkimageclassifier.utils.view.toPx
import com.sudzusama.vkimageclassifier.utils.view.visible

class DominantColorPalette : View {

    private var colors: List<DominantColor> = listOf()
    private val paint = Paint()
    private val path = Path()

    private var _bottomCornersRadius = 0f
    var topCornersRadius: Float
        get() = _topCornersRadius
        set(value) {
            _topCornersRadius = value
            invalidateCorners()
        }

    private var _topCornersRadius = 0f
    var bottomCornersRadius: Float
        get() = _bottomCornersRadius
        set(value) {
            _bottomCornersRadius = value
            invalidateCorners()
        }

    private var leftCorners = floatArrayOf(
        _topCornersRadius, _topCornersRadius,
        0f, 0f,
        0f, 0f,
        _bottomCornersRadius, _bottomCornersRadius
    )

    private var rightCorners = floatArrayOf(
        0f, 0f,
        _topCornersRadius, _topCornersRadius,
        _bottomCornersRadius, _bottomCornersRadius,
        0f, 0f
    )

    private var fullCorners = floatArrayOf(
        _topCornersRadius, _topCornersRadius,
        _topCornersRadius, _topCornersRadius,
        _bottomCornersRadius, _bottomCornersRadius,
        _bottomCornersRadius, _bottomCornersRadius
    )

    private val noCorners = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)


    private fun invalidateCorners() {
        leftCorners = floatArrayOf(
            _topCornersRadius, _topCornersRadius,
            0f, 0f,
            0f, 0f,
            _bottomCornersRadius, _bottomCornersRadius
        )
        rightCorners = floatArrayOf(
            0f, 0f,
            _topCornersRadius, _topCornersRadius,
            _bottomCornersRadius, _bottomCornersRadius,
            0f, 0f
        )
        fullCorners = floatArrayOf(
            _topCornersRadius, _topCornersRadius,
            _topCornersRadius, _topCornersRadius,
            _bottomCornersRadius, _bottomCornersRadius,
            _bottomCornersRadius, _bottomCornersRadius
        )
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DominantColorPalette, defStyle, 0)
        _topCornersRadius = a.getFloat(R.styleable.DominantColorPalette_topCorners, 0f).toPx
        _bottomCornersRadius = a.getFloat(R.styleable.DominantColorPalette_bottomCorners, 0f).toPx
        invalidateCorners()
        a.recycle()
    }

    fun setColors(rawColors: List<DominantColor>) {
        colors = rawColors
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (colors.isEmpty() && this.isVisible) invisible() else visible()

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        var left = 0f
        path.reset()
        paint.reset()

        colors.forEach {
            val itemWidth = contentWidth * it.percent
            paint.color = it.color
            canvas.drawRect(left, 0f, left + itemWidth, contentHeight.toFloat(), paint)
            left += itemWidth
        }

    }
}