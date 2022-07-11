package net.common.commonlib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import net.common.commonlib.utils.Constants
import java.lang.Exception

abstract class BaseFragment : Fragment() {

    private val TAG = "NfBaseFragment"
    lateinit var mBinding: ViewDataBinding
    var mView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mView = mBinding.root
        mView?.let {
            it.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    return true
                }
            })
        }
        return mView
    }

    protected abstract fun getLayoutId() : Int

    protected abstract fun getFragmentView(act: FragmentActivity, tag: String) : Fragment

    protected fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, backstack: Boolean, anim: Int) : Boolean {
        return AddFragment(act, whereId, tag, bundle = null, backstack, anim)
    }

    protected fun AddFragment(act: FragmentActivity, whereId: Int, tag: String, bundle: Bundle?, backstack: Boolean, anim: Int) : Boolean {
        var result = true

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            if (anim == Constants.ANIM_FRAGMENT_UP) {
                ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
            } else if (anim == Constants.ANIM_FRAGMENT_IN_LEFT) {
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_down)
            }

            val FMView = getFragmentView(act, tag)

            bundle?.let {
                FMView.arguments = bundle
            }

            ft.add(whereId, FMView, tag)

            check(backstack) {
                ft.addToBackStack(tag)
            }

            ft.commitAllowingStateLoss()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    protected fun RemoveViewTag(act: FragmentActivity, viewTag: String, anim: Int) : Boolean {
        return RemoveViewTag(act, arrayOf(viewTag), anim)
    }

    protected fun RemoveViewTag(act: FragmentActivity, viewTags: Array<String>, anim: Int) : Boolean {
        var isRemove = false

        try {
            val manager = act.supportFragmentManager
            val ft = manager.beginTransaction()

            if (anim == Constants.ANIM_FRAGMENT_UP) {
                ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
            } else if (anim == Constants.ANIM_FRAGMENT_IN_LEFT) {
                ft.setCustomAnimations(R.anim.slide_left, R.anim.slide_down)
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