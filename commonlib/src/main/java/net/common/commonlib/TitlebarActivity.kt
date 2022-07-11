package net.common.commonlib

import android.os.Bundle
import net.common.commonlib.view.BaseTitlebar

abstract class TitlebarActivity : BaseActivity() {

    protected var mTitlebar: BaseTitlebar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        mTitlebar = setTitlebar()
        initListener()
    }

    private fun initListener() {
        mTitlebar?.leftBtnCallback = {
            leftClickEvent()
        }

        mTitlebar?.rightBtnCallback = {
            rightClickEvent()
        }
    }

    protected open fun leftClickEvent() {}

    protected open fun rightClickEvent() {}

    protected abstract fun setTitlebar() : BaseTitlebar

}