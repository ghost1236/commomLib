package net.common.commonlib.network

import android.app.Activity
import android.util.Log
import net.common.commonlib.extension.MapExtension.asJSONObject
import net.common.commonlib.extension.StringExtension.asHashMap
import net.common.commonlib.extension.StringExtension.toAesEncoding
import net.common.commonlib.utils.Constants
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CescoApis {

    companion object {
        val instance by lazy { CescoApis() }
    }

    /**
     * 시리얼 번호 조회 API
     */
    fun matchSerialNumber(act: Activity, param: Map<String, Any>, successCallback: (Map<String, Any>)->Unit, failureCallback: (Map<String, Any>)->Unit) {
        val api = ApisRequest.getApi().matchSerialNumber(encordingMap(param))
        callApi(act, api, successCallback = successCallback, failureCallback = failureCallback)
    }

    private fun callApi(act: Activity, _api: Call<ResponseBody>, isShow: Boolean = true, isErrorToast: Boolean = false, isRetry: Boolean = true, successCallback: (Map<String, Any>)->Unit, failureCallback: (Map<String, Any>)->Unit) {
        ApisRequest.requestAPI(_api, isShow) { response ->
            if (response != null) {
                val responseMap = getResponseBody(response)
                if (!responseMap.isNullOrEmpty()) {
                    if (ApisResponse.isSuccess(act, responseMap)) {
                        onSuccess(responseMap, successCallback)
                    } else {
                        onFailure(responseMap, failureCallback)
                    }
                } else {
                    onFailure(HashMap(), failureCallback)
                }
            } else {
                // 통신에러.. 500
            }
        }
    }

    /**
     * 성공시 처리하는 부분
     */
    private fun onSuccess(responseMap: Map<String, Any>, successCallback: (Map<String, Any>)->Unit) {
        val body = ApisResponse.getBody(responseMap)
        successCallback?.invoke(body ?: HashMap())
    }

    /**
     * 실패시 처리하는 부분
     */
    private fun onFailure(responseMap: Map<String, Any>, failureCallback: (Map<String, Any>)->Unit) {
        val error = ApisResponse.getError(responseMap)
        failureCallback?.invoke(error ?: HashMap())
    }

    /**
     * ResponseBody -> HashMap<String, Any>
     */
    private fun getResponseBody(response: Response<ResponseBody>) : Map<String, Any>? {
        return if (response.isSuccessful) {
            val responseBody = response.body()?.string()
            responseBody?.asHashMap()
        } else {
            null
        }
    }

    /**
     * 통신 파라미터 생성
     *
     * key : e, value: Json String
     */
    private fun encordingMap(param: Map<String, Any>) : HashMap<String, Any> {
        return try {
            val map = hashMapOf<String, Any>()
            val jsonObj = param.asJSONObject()
            Log.d("OkHttp", "request param : $jsonObj")
            val json = jsonObj.toString().toAesEncoding(Constants.encKey).replace("\n","")
            map.put("e", json)
            map
        } catch (e: Exception) {
            e.printStackTrace()
            hashMapOf<String, Any>()
        }
    }

}