package net.common.commonlib.extension

import android.util.Log

/**
 * 로그 확장 — receiver 를 [Any] 로 두어 Activity / Application / Fragment / ViewModel / Service 등
 * **어디서든** 호출 가능. (기존 Activity 호출은 그대로 동작 — Activity 도 Any 의 하위라 하위호환)
 *
 * 본 함수들은 Activity 고유 기능을 쓰지 않고 [Log](static) + object 멤버([logStaus]/[baseTag])만
 * 사용하므로 receiver 가 Activity 일 이유가 없다.
 */
object LoggerExtension {
    var logStaus = false
    var baseTag = "Logger"

    var Any.logTag: String
        get() = baseTag
        set(value) {
            baseTag = value
        }

    var Any.logPrint: Boolean
        get() = logStaus
        set(value) {
            logStaus = value
        }

    fun Any.logD(a: String) {
        if (!logStaus) return
        logD(baseTag, a)
    }

    fun Any.logD(tag: String, a: String) {
        if (!logStaus) return
        Log.d(tag, a)
    }

    fun Any.logW(a: String) {
        if (!logStaus) return
        logW(baseTag, a)
    }

    fun Any.logW(tag: String, a: String) {
        if (!logStaus) return
        Log.w(tag, a)
    }

    fun Any.logI(a: String) {
        if (!logStaus) return
        logI(baseTag, a)
    }

    fun Any.logI(tag: String, a: String) {
        if (!logStaus) return
        Log.i(tag, a)
    }

    fun Any.logE(a: String) {
        if (!logStaus) return
        logE(baseTag, a)
    }

    fun Any.logE(tag: String, a: String) {
        if (!logStaus) return
        Log.e(tag, a)
    }
}
