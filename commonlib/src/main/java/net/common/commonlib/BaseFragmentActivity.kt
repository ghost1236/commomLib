package net.common.commonlib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import net.common.commonlib.utils.Constants
import java.lang.Exception

abstract class BaseFragmentActivity : FragmentActivity() {

    companion object {
        lateinit var activity: Activity
        lateinit var mBinding: ViewDataBinding
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun getFragmentView(tag: String): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        activity = this

    }

    /**
     * 액티비티를 실행하는 함수
     */
    fun startActivity(intent : Intent, enterAnyId : Int? = 0, exitAnyId : Int? = 0, isFinish: Boolean) {
        startActivity(intent)
        overridePendingTransition(enterAnyId ?: 0, exitAnyId ?: 0)
        if (isFinish) {
            finish()
        }
    }

    /**
     * 액티비티를 실행하는 함수 (ActivityResultLauncher)
     */
    fun startActivityResultLauncher(intent: Intent, enterAnyId : Int? = 0, exitAnyId : Int? = 0, launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(intent)
        overridePendingTransition(enterAnyId ?: 0, exitAnyId ?: 0)
    }

    fun finishActivity(enterAnyId : Int? = 0, exitAnyId : Int? = 0) {
        finish()
        overridePendingTransition(enterAnyId ?: 0, exitAnyId ?: 0)
    }

    fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, backstack: Boolean, anim: Int) : Boolean {
        return AddFragment(act, whereId, tag, bundle = null, backstack, anim)
    }

    fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, bundle: Bundle?, backstack: Boolean, anim: Int) : Boolean {
        var result = true

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            if (anim == Constants.ANIM_FRAGMENT_UP) {
                ft.setCustomAnimations(R.anim.slide_up, R.anim.ani_none)
            } else if (anim == Constants.ANIM_FRAGMENT_IN_LEFT) {
                ft.setCustomAnimations(R.anim.slide_left, R.anim.ani_none)
            }

            val FMView = getFragmentView(tag)

            bundle?.let {
                FMView.arguments = bundle
            }

            ft.add(whereId, FMView, tag)

            if (backstack) {
                ft.addToBackStack(tag)
            }

            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun replaceFragment(act: FragmentActivity, whereId: Int, tag: String, backstack: Boolean, anim: Int) : Boolean {
        return replaceFragment(act, whereId, tag, bundle = null, backstack, anim)
    }

    fun replaceFragment(act: FragmentActivity, whereId: Int, tag: String, bundle: Bundle?, backstack: Boolean, anim: Int) : Boolean {
        var result = true

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            if (anim == Constants.ANIM_FRAGMENT_UP) {
                ft.setCustomAnimations(R.anim.ani_none, R.anim.slide_down)
            } else if (anim == Constants.ANIM_FRAGMENT_IN_LEFT) {
                ft.setCustomAnimations(R.anim.slide_left, R.anim.ani_none)
            }

            val FMView = getFragmentView(tag)

            bundle?.let {
                FMView.arguments = bundle
            }

            ft.replace(whereId, FMView, tag)

            if (backstack) {
                ft.addToBackStack(tag)
            }

            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun RemoveViewTag(act: FragmentActivity, viewTag: String, anim: Int) : Boolean {
        return RemoveViewTag(act, arrayOf(viewTag), anim)
    }

    fun RemoveViewTag(act: FragmentActivity, viewTags: Array<String>, anim: Int) : Boolean {
        var isRemove = false

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            if (anim == Constants.ANIM_FRAGMENT_UP) {
                ft.setCustomAnimations(R.anim.ani_none, R.anim.slide_down)
            } else if (anim == Constants.ANIM_FRAGMENT_IN_LEFT) {
                ft.setCustomAnimations(R.anim.ani_none, R.anim.slide_right)
            }

            for (viewTag in viewTags) {
                val pageView: Fragment? = manager.findFragmentByTag(viewTag)
                pageView?.let {
                    ft.remove(it)
                    isRemove = true
                }
            }

            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isRemove
    }
}