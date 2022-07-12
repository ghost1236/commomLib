package net.common.commonlib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import net.common.commonlib.extension.MapExtension.asString
import net.common.commonlib.network.CescoApis
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        lateinit var activity: Activity
        lateinit var mBinding: ViewDataBinding
    }

    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        activity = this


        matchSerialNumber()
    }

    private fun matchSerialNumber() {

        val successCallback = { result: Map<String, Any> ->

        }

        val failureCallback = { result: Map<String, Any> ->

        }

        var param = hashMapOf<String, Any>()
        param["serialNumber"] = "v2012682109130664"

        CescoApis.instance.matchSerialNumber(this, param, successCallback, failureCallback)
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
}