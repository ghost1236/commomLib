package net.common.commonlib.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApisInterface {

    /**
     * 시리얼 번호 조회
     */
    @FormUrlEncoded
    @POST("device/matchSerialNumber")
    fun matchSerialNumber(@FieldMap param: HashMap<String, Any>): Call<ResponseBody>
}