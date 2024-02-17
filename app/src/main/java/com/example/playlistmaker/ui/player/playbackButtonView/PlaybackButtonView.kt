package com.example.playlistmaker.ui.player.playbackButtonView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.example.playlistmaker.R
import org.checkerframework.checker.units.qual.min
import org.w3c.dom.Attr
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?=null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val minSize = resources.getDimensionPixelSize(R.dimen.playbackButtonSize)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
}