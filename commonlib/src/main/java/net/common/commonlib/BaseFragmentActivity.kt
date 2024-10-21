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

    fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, backstack: Boolean, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        return AddFragment(act, whereId, tag, bundle = null, backstack, enterAnyId ?: 0, exitAnyId ?: 0)
    }

    fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, bundle: Bundle?, backstack: Boolean, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        var result = true

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            ft.setCustomAnimations(enterAnyId ?: 0, exitAnyId ?: 0)

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

    fun replaceFragment(act: FragmentActivity, whereId: Int, tag: String, backstack: Boolean, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        return replaceFragment(act, whereId, tag, bundle = null, backstack, enterAnyId ?: 0, exitAnyId ?: 0)
    }

    fun replaceFragment(act: FragmentActivity, whereId: Int, tag: String, bundle: Bundle?, backstack: Boolean, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        var result = true

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            ft.setCustomAnimations(enterAnyId ?: 0, exitAnyId ?: 0)

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

    fun RemoveViewTag(act: FragmentActivity, viewTag: String, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        return RemoveViewTag(act, arrayOf(viewTag), enterAnyId ?: 0, exitAnyId ?: 0)
    }

    fun RemoveViewTag(act: FragmentActivity, viewTags: Array<String>, enterAnyId : Int? = 0, exitAnyId : Int? = 0) : Boolean {
        var isRemove = false

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            ft.setCustomAnimations(enterAnyId ?: 0, exitAnyId ?: 0)

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