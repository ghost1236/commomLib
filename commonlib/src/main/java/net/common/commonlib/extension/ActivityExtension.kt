package net.common.commonlib.extension

import android.app.Activity
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import net.common.commonlib.extension.utils.Prefs
import java.text.SimpleDateFormat
import java.util.*

object ActivityExtension {

    fun Activity.deviceId() : String {
        val deviceId: String = Prefs.getString(this, Prefs.DEVCIE_ID, "")
        val value = if (deviceId.isEmpty()) {
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            deviceId
        }
        Prefs.putString(this, Prefs.DEVCIE_ID, value)
        return value
    }

    fun Activity.osVersion() : String = android.os.Build.VERSION.RELEASE

    fun Activity.model() : String = Build.MODEL

    fun Activity.lang() : String = getLocale().language

    fun Activity.country() : String =  getLocale().country

    fun Activity.gmt() : String {
        val timeZone = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault()).let {
            SimpleDateFormat("Z").format(it.time)
        }
        return timeZone.substring(0,3) + ":" + timeZone.substring(3,5)
    }

    private fun Activity.getLocale() : Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
    }

    fun Activity.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}