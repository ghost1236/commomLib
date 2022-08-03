package net.common.commonlib.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import net.common.commonlib.R

abstract class BaseTitlebar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var leftBtnCallback: (()-> Unit)? = null
    var rightBtnCallback: (()-> Unit)? = null

    init {
        addView(getLayoutView())
        getAttr(attrs, defStyle)
    }

    protected abstract fun getLayoutView(): View?

    private fun getAttr(attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BaseTitlebar, defStyle, 0)

        val bgColor =
            typedArray.getResourceId(R.styleable.BaseTitlebar_titlebarBg, R.color.color_4285f4)
        setTitleBgColor(bgColor)

        val titleStr = typedArray.getString(R.styleable.BaseTitlebar_titleText)
        titleStr?.let { setTitle(it) }

        val bgLeftBtn = typedArray.getDrawable(R.styleable.BaseTitlebar_leftImgBg)
        bgLeftBtn?.let { setLeftBg(it) }

        val bgRightBtn = typedArray.getDrawable(R.styleable.BaseTitlebar_rightImgBg)
        bgRightBtn?.let { setRightBg(it) }
        val leftVisibility = typedArray.getInt(R.styleable.BaseTitlebar_leftBtnShow, GONE)
        setLeftBtnVisiable(leftVisibility)

        val rightVisibility = typedArray.getInt(R.styleable.BaseTitlebar_rightBtnShow, GONE)
        setRightBtnVisiable(rightVisibility)
    }

    protected abstract fun setTitleBgColor(color: Int)

    protected abstract fun setTitle(title: String)

    protected abstract fun setLeftBg(drawable: Drawable)

    protected abstract fun setRightBg(drawable: Drawable)

    protected abstract fun setLeftBtnVisiable(visiable: Int)

    protected abstract fun setRightBtnVisiable(visiable: Int)
}