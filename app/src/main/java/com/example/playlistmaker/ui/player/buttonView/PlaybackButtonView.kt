package com.example.playlistmaker.ui.player.buttonView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imagePlay: Bitmap?
    private var imagePause: Bitmap?
    private var imageToShow: Bitmap? = null

    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false
    var onTouchListener: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlay = getDrawable(R.styleable.PlaybackButtonView_imagePlay)?.toBitmap()
                imagePause = getDrawable(R.styleable.PlaybackButtonView_imagePause)?.toBitmap()
                imageToShow = imagePlay
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (imageToShow != null) {
            canvas.drawBitmap(imageToShow!!, null, imageRect, null)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return true
                }

                MotionEvent.ACTION_UP -> {
                    playerSwitch()
                    invalidate()
                    return true
                }
            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

    private fun playerSwitch() {
        onTouchListener?.invoke()
        if (!isPlaying) {
            imageToShow = imagePause
            isPlaying = true
        } else {
            imageToShow = imagePlay
            isPlaying = false
        }
    }

    fun onStopped() {
        imageToShow = imagePlay
        isPlaying = false
        invalidate()
        Log.d("КастомВью", "onStopped")
    }
}