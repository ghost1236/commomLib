package net.common.commonlib.network

import android.app.Activity
import android.content.Context
import android.widget.Toast
import net.common.commonlib.extension.ActivityExtension.showToast
import net.common.commonlib.extension.MapExtension.asHashMap
import net.common.commonlib.extension.MapExtension.asString

object ApisResponse {

    open val RESULT_CODE = "code"
    open val RESULT_MESSAGE = "msg"

    private val KEY_HEADER = "header"
    private val KEY_BODY = "body"

    open var RESULT_SUCCESS = "200"
    open var NOT_DEVICE = "3000"
    open var MATCHED_DEVICE = "3001"

    open fun isSuccess(act: Activity, result: Map<String, Any>, isErrorToast: Boolean? = false) : Boolean {
        val header = result[KEY_HEADER] as Map<String, Any>
        var isSuccessYn = if (header.isNotEmpty()) {
            val code = header.asString(RESULT_CODE)
            if (RESULT_SUCCESS.equals(code, true)) {
                true
            } else {
                if (isErrorToast == true) {
                    header.asString(RESULT_MESSAGE)?.let { act.showToast(it) }
                }
                false
            }
        } else {
            false
        }

        return isSuccessYn
    }

    open fun getBody(result: Map<String, Any>) : Map<String, Any>? {
        return result.asHashMap(KEY_BODY)
    }

    open fun getError(result: Map<String, Any>) : Map<String, Any>? {
        return result.asHashMap(KEY_HEADER)
    }
}