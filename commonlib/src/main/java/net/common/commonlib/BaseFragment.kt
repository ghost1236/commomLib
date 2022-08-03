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

}