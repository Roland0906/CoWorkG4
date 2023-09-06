package app.appworks.school.stylish

import  android.content.Context
import  android.graphics.Canvas
import  android.graphics.Color
import  android.graphics.Paint
import  android.os.Handler
import  android.os.Looper
import  android.os.Message
import  android.text.TextPaint
import  android.util.AttributeSet
import  android.view.View
import  androidx.annotation.ColorInt
import  androidx.annotation.FloatRange
import  androidx.annotation.Px
import  java.lang.ref.WeakReference
import  kotlin.math.abs
import  kotlin.math.ceil

open class MarqueeTextView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "MarqueeTextView"
        const val BLANK = " "
        const val REPEAT_SINGLE = 1  //一次結束
        const val REPEAT_SINGLE_LOOP = 0  //單個循序
        const val REPEAT_FILL_LOOP = -1  // 填充後循環
    }


    var speed = 1f
        set(value) {
            if (value <= 0) {
                field = 0f
                stop()
            } else {
                field = value
            }
        }


    var text = ""
        set(value) {
            if (field.isEmpty() && value.isEmpty()) {
                return
            }
            field = value
            var targetContent = value
            if (isResetLocation) {
                xLocation = width * startLocationDistance
            }
            val itemEndBlank = getItemEndBlank()
            if (!targetContent.endsWith(itemEndBlank)) {
                targetContent += itemEndBlank
            }

            if (repeat == REPEAT_FILL_LOOP) {
                mFinalDrawText = ""

                mSingleContentWidth = getTextWidth(targetContent)
                if (mSingleContentWidth > 0) {

                    val maxVisibleCount = ceil(width / mSingleContentWidth.toDouble()).toInt() + 1
                    repeat(maxVisibleCount) {
                        mFinalDrawText += targetContent
                    }
                }
                contentWidth = getTextWidth(mFinalDrawText)
            } else {
                if (xLocation < 0 && repeat == REPEAT_SINGLE) {
                    if (abs(xLocation) > contentWidth) {
                        xLocation = width * startLocationDistance
                    }
                }
                mFinalDrawText = targetContent
                contentWidth = getTextWidth(mFinalDrawText)
                mSingleContentWidth = contentWidth
            }
            textHeight = getTextHeight()
            invalidate()
        }

    private var mFinalDrawText: String = ""


    @ColorInt
    var textColor = Color.WHITE
        set(value) {
            if (value != field) {
                field = value
                textPaint.color = textColor
                invalidate()
            }
        }


    @Px
    var textSize = 12f
        set(value) {
            if (value > 0 && value != field) {
                field = value
                textPaint.textSize = value
                if (text.isNotEmpty()) {
                    text = text
                }
            }
        }


    @Px
    var textItemDistance = 50f
        set(value) {
            if (field == value) {
                return
            }
            field = if (value < 0f) 0f else value
            if (text.isNotEmpty()) {
                text = text
            }
        }


    var repeat = REPEAT_SINGLE_LOOP
        set(value) {
            if (value != field) {
                field = value
                resetInit = true
                text = text
            }
        }

    @FloatRange(from = 0.0, to = 1.0)
    var startLocationDistance = 0.0f
        set(value) {
            if (value == field) {
                return
            }
            field = when {
                value < 0f -> 0f
                value > 1f -> 1f
                else -> value
            }
        }


    var isResetLocation = false
    private var xLocation = 0f  //文本的x坐標


    private var mSingleContentWidth: Float = 0f


    private var contentWidth = 0f


    var isRolling = false
        private set


    protected val textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var textHeight = 0f
    private var resetInit = true

    private val mHandler by lazy { MyHandler(this) }

    /**
     * 是否用戶主動調用，默認true
     */
    var isRollByUser = true

    init {
        initAttrs(attrs)
        initPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (resetInit && text.isNotEmpty()) {
            textItemDistance = textItemDistance
            xLocation = width * startLocationDistance
            resetInit = false
        }
        val absLocation = abs(xLocation)
        when (repeat) {
            REPEAT_SINGLE -> if (contentWidth < absLocation) {
                stop()
            }

            REPEAT_SINGLE_LOOP -> if (contentWidth <= absLocation) {

                xLocation = width.toFloat()
            }

            REPEAT_FILL_LOOP -> if (xLocation < 0 && mSingleContentWidth <= absLocation) {
                xLocation = mSingleContentWidth - absLocation
            }

            else ->
                if (contentWidth < absLocation) {
                    stop()
                }
        }

        if (mFinalDrawText.isNotBlank()) {
            canvas.drawText(mFinalDrawText, xLocation, height / 2 + textHeight / 2, textPaint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isRollByUser) {
            startInternal(true)
        }
    }

    override fun onDetachedFromWindow() {
        if (isRolling) {
            stopInternal(false)
        }
        super.onDetachedFromWindow()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility != VISIBLE) {
            stopInternal(false)
        } else {
            if (!isRollByUser) {
                startInternal(false)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        text = text
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeTextView)
        textColor = a.getColor(R.styleable.MarqueeTextView_android_textColor, textColor)
        isResetLocation = a.getBoolean(R.styleable.MarqueeTextView_marqueeResetLocation, true)
        speed = a.getFloat(R.styleable.MarqueeTextView_marqueeSpeed, 1f)
        textSize = a.getDimension(R.styleable.MarqueeTextView_android_textSize, 12f)
        textItemDistance = a.getDimension(R.styleable.MarqueeTextView_marqueeItemDistance, 50f)
        startLocationDistance = a.getFloat(
            R.styleable.MarqueeTextView_marqueeStartLocationDistance,
            0f
        )
        repeat = a.getInt(R.styleable.MarqueeTextView_marqueeRepeat, REPEAT_SINGLE_LOOP)
        text = a.getText(R.styleable.MarqueeTextView_android_text)?.toString() ?: ""
        a.recycle()
    }


    private fun initPaint() {
        textPaint.apply {
            style = Paint.Style.FILL
            color = textColor
            textSize = textSize
            isAntiAlias = true
            density = 1 / resources.displayMetrics.density
        }
    }

    fun toggle() {
        if (isRolling) {
            stop()
        } else {
            start()
        }
    }


    fun start() {
        startInternal(true)
    }


    protected fun startInternal(isRollByUser: Boolean) {
        this.isRollByUser = isRollByUser
        stop()
        if (text.isNotBlank()) {
            mHandler.sendEmptyMessage(MyHandler.WHAT_RUN)
            isRolling = true
        }
    }

    fun stop() {
        stopInternal(true)
    }


    protected fun stopInternal(isRollByUser: Boolean) {
        this.isRollByUser = isRollByUser
        isRolling = false
        mHandler.removeMessages(MyHandler.WHAT_RUN)
    }


    private fun getBlankWidth(): Float {
        return getTextWidth(BLANK)
    }

    private fun getTextWidth(text: String?): Float {
        if (text.isNullOrEmpty()) {
            return 0f
        }
        return textPaint.measureText(text)
    }

    private fun getTextHeight(): Float {
        val fontMetrics = textPaint.fontMetrics
        return abs(fontMetrics.bottom - fontMetrics.top) / 2
    }

    private fun getItemEndBlank(): String {
        val oneBlankWidth = getBlankWidth()  //空格的寬度
        var count = 1
        if (textItemDistance > 0 && oneBlankWidth != 0f) {
            count = (ceil(textItemDistance / oneBlankWidth).toInt())  //粗略計算空格數量
        }
        val builder = StringBuilder(count)
        for (i in 0..count) {
            builder.append(BLANK) //間隔字符串
        }
        return builder.toString()
    }


    private class MyHandler(view: MarqueeTextView) : Handler(Looper.getMainLooper()) {

        companion object {
            internal const val WHAT_RUN = 1001
        }

        private val mRef = WeakReference(view)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == WHAT_RUN) {
                mRef.get()?.apply {
                    if (speed > 0) {
                        xLocation -= speed
                        invalidate()
                        // 50 毫秒繪製一次
                        sendEmptyMessageDelayed(WHAT_RUN, 50)
                    }
                }
            }
        }
    }

}