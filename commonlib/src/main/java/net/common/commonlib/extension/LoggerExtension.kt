package net.common.commonlib.extension

import android.app.Activity
import android.util.Log

object LoggerExtension {
    var logStaus = false
    var baseTag = "Logger"

    var Activity.logTag : String
        get() = baseTag
        set(value) {
            baseTag = value
        }

    var Activity.logPrint : Boolean
        get() = logStaus
        set(value) {
            logStaus = value
        }

    fun Activity.logD(a: String) {
        if (!logStaus)  return
        logD(baseTag, a)
    }

    fun Activity.logD(tag: String, a: String) {
        if (!logStaus)  return
        if (tag == null) Log.d(baseTag, a) else Log.d(tag, a)
    }

    fun Activity.logW(a: String) {
        if (!logStaus)  return
        logW(baseTag, a)
    }

    fun Activity.logW(tag: String, a: String) {
        if (!logStaus)  return
        if (tag == null) Log.w(baseTag, a) else Log.w(tag, a)
    }

    fun Activity.logI(a: String) {
        if (!logStaus)  return
        logI(baseTag, a)
    }

    fun Activity.logI(tag: String, a: String) {
        if (!logStaus)  return
        if (tag == null) Log.i(baseTag, a) else Log.i(tag, a)
    }

    fun Activity.logE(a: String) {
        if (!logStaus)  return
        logE(baseTag, a)
    }

    fun Activity.logE(tag: String, a: String) {
        if (!logStaus)  return
        if (tag == null) Log.e(baseTag, a) else Log.e(tag, a)
    }

}

