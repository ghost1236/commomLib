package net.common.commonlib.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView


@SuppressLint("AppCompatCustomView")
class AutoResizeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle) {

    // Minimum text size for this text view
    val MIN_TEXT_SIZE: Float = 20f

    // Our ellipse string
    private val mEllipsis = "..."

    // Registered resize listener
    private var mTextResizeListener: OnTextResizeListener? = null

    // Flag for text and/or size changes to force a resize
    private var mNeedsResize = false

    // Text size that is set from code. This acts as a starting point for resizing
    private var mTextSize = 0f

    // Temporary upper bounds on the starting text size
    private var mMaxTextSize = 0f

    // Lower bounds for text size
    private var mMinTextSize = MIN_TEXT_SIZE

    // Text view line spacing multiplier
    private var mSpacingMult = 1.0f

    // Text view additional line spacing
    private var mSpacingAdd = 0.0f

    // Add ellipsis to text that overflows at the smallest text size
    private var mAddEllipsis = true


    interface OnTextResizeListener {
        abstract fun onTextReSize(textView: TextView, oldSize: Float, newSize: Float)
    }

    init {
        mTextSize = textSize
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        mNeedsResize = true
        resetTextSize()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            mNeedsResize = true
        }
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        mTextSize = textSize
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        mTextSize = textSize
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        super.setLineSpacing(add, mult)
        mSpacingMult = mult
        mSpacingAdd = add
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed || mNeedsResize) {
            var widthLimit = (right - left) - compoundPaddingLeft - compoundPaddingRight
            var heightLimit = (bottom - top) - compoundPaddingBottom - compoundPaddingTop
            resizeText(widthLimit, heightLimit)
        }
        super.onLayout(changed, left, top, right, bottom)
    }


    fun setMaxTextSize(maxTextSize: Float) {
        mMaxTextSize = maxTextSize
        requestLayout()
        invalidate()
    }

    fun getMaxTextSize() : Float = mMaxTextSize

    fun setMinTextSize(minTextSize: Float) {
        mMinTextSize = minTextSize
        requestLayout()
        invalidate()
    }

    fun getMinTextSize() : Float = mMinTextSize

    fun setAddEllipsis(addEllipsis: Boolean) {
        mAddEllipsis = addEllipsis
    }

    fun getAddEllipsis() : Boolean = mAddEllipsis

    fun setOnResizeListener(listener: (TextView, Float, Float) -> Unit) {
        this.mTextResizeListener = object : OnTextResizeListener {
            override fun onTextReSize(textView: TextView, oldSize: Float, newSize: Float) {
                listener(textView, oldSize, newSize)
            }
        }
    }

    /**
     * Reset the text to the original size
     */
    fun resetTextSize() {
        if (mTextSize > 0) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            mMaxTextSize = mTextSize
        }
    }

    fun resizeText() {
        val heightLimit = height - paddingBottom - paddingTop
        val widthLimit = width - paddingLeft - paddingRight
        resizeText(widthLimit, heightLimit)
    }

    fun resizeText(width: Int, height: Int) {
        var charText = text
        if (text == null || length() == 0 || height <= 0 || width <= 0 || mTextSize == 0f) {
            return;
        }

        transformationMethod?.let {
            charText = it.getTransformation(charText, this)
        }

        var textPaint = paint

        var oldTextSize = textPaint.textSize
        var targetTextSize = if (mMaxTextSize > 0) Math.min(mTextSize, mMaxTextSize) else mTextSize

        var textHeight = getTextHeight(charText, textPaint, width, targetTextSize)
        while (textHeight > height && targetTextSize > mMinTextSize) {
            targetTextSize = Math.max(targetTextSize - 2, mMinTextSize);
            textHeight = getTextHeight(text, textPaint, width, targetTextSize);
        }

        if (mAddEllipsis && targetTextSize === mMinTextSize && textHeight > height) {
            // Draw using a static layout
            // modified: use a copy of TextPaint for measuring
            val paint = TextPaint(textPaint)
            // Draw using a static layout

            var layout: StaticLayout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layout = StaticLayout.Builder.obtain(charText, 0, charText.length, paint, width).apply {
                    setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    setLineSpacing(mSpacingMult, mSpacingAdd)
                    setIncludePad(false)
                }.build()
            } else {
                layout = StaticLayout(charText, paint, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false)
            }


            // Check that we have a least one line of rendered text
            if (layout.lineCount > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line
                val lastLine = layout.getLineForVertical(height) - 1
                // If the text would not even fit on a single line, clear it
                if (lastLine < 0) {
                    text = ""
                } else {
                    val start = layout.getLineStart(lastLine)
                    var end = layout.getLineEnd(lastLine)
                    var lineWidth = layout.getLineWidth(lastLine)
                    val ellipseWidth = textPaint.measureText(mEllipsis)

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (width < lineWidth + ellipseWidth) {
                        lineWidth =
                            textPaint.measureText(text.subSequence(start, --end + 1).toString())
                    }
                    text = text.subSequence(0, end).toString() + mEllipsis
                }
            }
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, targetTextSize);
        setLineSpacing(mSpacingAdd, mSpacingMult);

        mTextResizeListener?.let {
            it.onTextReSize(this, oldTextSize, targetTextSize)
        }

        mNeedsResize = false
    }

    private fun getTextHeight(source: CharSequence, paint: TextPaint, width: Int, textSize: Float) : Int {
        val paintCopy = TextPaint(paint)
        paintCopy.textSize = textSize

        var layout: StaticLayout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout = StaticLayout.Builder.obtain(source, 0, source.length, paintCopy, width).apply {
                setAlignment(Layout.Alignment.ALIGN_NORMAL)
                setLineSpacing(mSpacingMult, mSpacingAdd)
                setIncludePad(true)
            }.build()
        } else {
            layout = StaticLayout(source, paintCopy, width, Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true)
        }
        return layout.height
    }

}