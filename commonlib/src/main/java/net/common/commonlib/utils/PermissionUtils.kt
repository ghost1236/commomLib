package net.common.commonlib.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

abstract class PermissionUtils {

    val PERMISSION_TYPE_ESSENTIAL = "Essential"
    val PERMISSION_TYPE_OPTION = "Optional"

    var essentialList: Array<String>? = setEssentialList()
    var optionalList: Array<String>? = setOptionalList()

    abstract fun setEssentialList() : Array<String>
    abstract fun setOptionalList() : Array<String>

    var requestPopupWasRunning = false  // 권한 요청 팝업 실행 여부 확인
    var deniedPopupWasRunning = false   // 거부 안내 팝업 실행 여부 확인


    /** 모든 필수 권한이 있는지 확인한다.
     * @return 필수 권한이 모두 있는 경우 true */
    fun checkEssential(activity: Activity) : Boolean {
        val list: Array<String> = essentialList?.let { it } ?: arrayOf()

        list.forEach {
            if (!checkPermission(activity, it)) {
                return false
            }
        }
        return true
    }

    fun checkOptional(activity: Activity) : Boolean {
        val list: Array<String> = optionalList?.let { it } ?: arrayOf()

        list.forEach {
            if (!checkPermission(activity, it)) {
                return false
            }
        }
        return true
    }

    /** 권한 허용 여부를 확인한다.
     * @param permission : 확인할 권한
     * @return 권한이 있으면 true */
    fun checkPermission(activity: Activity, permission: String) : Boolean {
        val state = if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q && Manifest.permission.ACCESS_BACKGROUND_LOCATION == permission) {
                true
            }
            false
        }

        return state
    }

    fun checkPermissions(activity: Activity, permissions: Array<String>) : Boolean {
        var isGrant = true

        permissions.forEach {
            if (!checkPermission(activity, it)) {
                return false
            }
        }
        return true
    }

    /** 거부된 필수권한 목록을 가온다.
     * @return 거부된 필수권한 리스트 */
    fun getEssentialDeniedList(activity: Activity) : List<String> {
        var rList = arrayListOf<String>()

        val list: Array<String> = essentialList?.let { it } ?: arrayOf()

        list.forEach {
            if (!checkPermission(activity, it)) {
                rList.add(it)
            }
        }
        return rList
    }

    /** 다시보지 않기로 거부된 경우
     * @param permission : 확인할 권한
     * @return 사용자가 거부를 선택한 경우 true / 사용자가 다시 보지 않기를 선택하고 거부를 누른 경우와 다시 보지 않기 때문에 거부된 경우 false */
    @RequiresApi(Build.VERSION_CODES.M)
    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String) : Boolean {
        return activity.shouldShowRequestPermissionRationale(permission)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getEssentialDeniedShouldShowList(activity: Activity) : List<String> {
        var rList = arrayListOf<String>()

        getEssentialDeniedList(activity).forEach {
            if (!shouldShowRequestPermissionRationale(activity, it)) {
                rList.add(it)
            }
        }
        return rList
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        try {
            requestPopupWasRunning = true
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}


