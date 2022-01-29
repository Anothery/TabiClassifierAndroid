package com.sudzusama.vkimageclassifier.utils.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.URLSpan
import androidx.core.graphics.ColorUtils
import com.google.android.material.chip.ChipDrawable
import com.sudzusama.vkimageclassifier.R
import java.util.regex.Pattern
import javax.inject.Inject


class VkSpannableHelper @Inject constructor(private val context: Context) {
    fun formatToSpannable(text: String): SpannableStringBuilder {
        val spannable = SpannableStringBuilder()
        val sb = StringBuffer()
        // Match urls
        val urlMatcher = Pattern.compile("\\[(.*?)\\|(.*?)]").matcher(text)
        while (urlMatcher.find()) {
            sb.setLength(0)
            val spanText = urlMatcher.group(2) ?: ""
            urlMatcher.appendReplacement(sb, spanText)
            spannable.append(sb.toString())
            spannable.setSpan(
                URLSpan(urlMatcher.group(1)),
                spannable.length - spanText.length,
                spannable.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        sb.setLength(0)
        urlMatcher.appendTail(sb)
        spannable.append(sb.toString())

        // Match tags
        val tagMatcher = Pattern.compile("#\\w+(@[\\w]*)?").matcher(spannable)
        while (tagMatcher.find()) {
            sb.setLength(0)
            spannable.append(sb.toString())
            val colorName = tagMatcher.group().replace("#", "")
            val color = getColorByName(colorName)
            val chip = ChipDrawable.createFromResource(
                context,
                if (isLightColor(color)) R.xml.light_chip else R.xml.dark_chip
            )
            chip.text = colorName
            chip.chipBackgroundColor = ColorStateList.valueOf(color)
            if (color == Color.WHITE) {
                chip.chipStrokeWidth = 2.toPx.toFloat()
                chip.chipStrokeColor =
                    ColorStateList.valueOf(ColorUtils.blendARGB(color, Color.BLACK, 0.1f))
            }

            chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)
            spannable.setSpan(
                ChipSpan(chip),
                tagMatcher.start(),
                tagMatcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }

    private fun getColorByName(color: String): Int {
        return when (color) {
            "bw" -> Color.BLACK
            "mixed" -> Color.parseColor("#3c9970")
            "white" -> Color.WHITE
            "black" -> Color.BLACK
            "gray" -> Color.GRAY
            "red" -> Color.parseColor("#DC143C")
            "orange" -> Color.parseColor("#FFA500")
            "pink" -> Color.parseColor("#FFC0CB")
            "violet" -> Color.parseColor("#7a41bb")
            "cyan" -> Color.parseColor("#AFEEEE")
            "blue" -> Color.parseColor("#4169E1")
            "yellow" -> Color.parseColor("#FFFF99")
            "green" -> Color.parseColor("#228B22")
            "gold" -> Color.parseColor("#FFD700")
            "beige" -> Color.parseColor("#F5F5DC")
            "brown" -> Color.parseColor("#7e5331")
            else -> Color.LTGRAY
        }
    }

    private fun isLightColor(color: Int): Boolean {
        val luminance = (0.299 * Color.red(color) + 0.587 * Color.green(color)
                + 0.114 * Color.blue(color)) / 255
        return luminance > 0.6
    }
}