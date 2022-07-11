package net.common.commonlib.network

import android.app.Activity
import net.common.commonlib.BaseActivity
import net.common.commonlib.utils.Constants
import net.common.commonlib.utils.Prefs
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApisRequest : InterceptorExtension() {

    fun getApi() : ApisInterface {
        return Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApisInterface::class.java)
    }

    private val client
        get() = try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            OkHttpClient.Builder().apply {
                sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                hostnameVerifier { _, _ -> true }
                followSslRedirects(true)
                followRedirects(true)
                connectTimeout(1, TimeUnit.MINUTES)
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                addInterceptor(RequestCookiesInterceptor(BaseActivity.activity))
                addInterceptor(getHttpLoggingInterceptor())
            }.build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    fun requestAPI(api: Call<ResponseBody>, isShow: Boolean, callback: (retrofit2.Response<ResponseBody>?)->Unit) {
        api.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                callback.invoke(response)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }
}

open class InterceptorExtension {

    fun ApisRequest.getHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.HEADERS)
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    class RequestCookiesInterceptor(activity: Activity) : Interceptor {
        private val act = activity
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.addHeader("Cookie", Prefs.getString(act, Prefs.COOKIES_STR, ""))

            return chain.proceed(builder.build())
        }
    }
}