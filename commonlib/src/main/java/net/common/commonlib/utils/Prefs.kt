package net.common.commonlib.utils

import android.content.Context
import android.content.SharedPreferences
import net.common.commonlib.utils.JsonParser
import org.json.JSONObject

object Prefs {
    val M_PRIVATE = "private"
    val M_PUBLIC = "public"

    var DEVCIE_ID = "device_id"
    const val COOKIES_STR = "cookies_str"

    private fun get(context: Context, mainKey : String) : SharedPreferences{
        return context.getSharedPreferences(mainKey, Context.MODE_PRIVATE)
    }

    fun putStringSet(context: Context, key : String, defValue : MutableSet<String>) {
        putStringSet(context, M_PRIVATE, key, defValue)
    }

    fun putStringSet(context: Context, mainKey: String, subKey : String, defValue : MutableSet<String>) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putStringSet(subKey, defValue)
        editor.commit()
    }

    fun getStringSet(context: Context, key: String, defValue: Set<String>) : MutableSet<String>?{
        return getStringSet(context, key, defValue)
    }

    fun getStringSet(context: Context, mainKey: String, subKey: String, defValue: Set<String>) : MutableSet<String>? {
        return get(context, mainKey).getStringSet(subKey, defValue)
    }

    fun putBoolean(context: Context, key : String, defValue: Boolean) {
        putBoolean(context, M_PRIVATE, key, defValue)
    }

    fun putBoolean(context: Context, mainKey: String, subKey: String, defValue: Boolean) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putBoolean(subKey, defValue)
        editor.commit()
    }

    fun getBoolean(context: Context, key: String, defValue: Boolean) : Boolean {
        return getBoolean(context, M_PRIVATE, key, defValue)
    }

    fun getBoolean(context: Context, mainKey: String, subKey: String, defValue: Boolean) : Boolean {
        return get(context, mainKey).getBoolean(subKey, defValue)
    }

    fun putString(context: Context, key: String, defValue: String) {
        putString(context, M_PRIVATE, key, defValue)
    }

    fun putString(context: Context, mainKey: String, subKey: String, defValue: String) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        if (defValue.equals("")) {
            editor.putString(subKey, "")
        } else {
            editor.putString(subKey, defValue)
        }
        editor.commit()
    }

    fun getString(context: Context, key: String, defValue: String) : String {
        return getString(context, M_PRIVATE, key, defValue)
    }

    fun getString(context: Context, mainKey: String, subKey: String, defValue: String): String {
        val str: String? = get(context, mainKey).getString(subKey, defValue)
        return str?.let { it } ?: ""
    }

    fun putLong(context: Context, key : String, defValue: Long) {
        putLong(context, key, defValue)
    }

    fun putLong(context: Context, mainKey: String, subKey: String, defValue: Long) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putLong(subKey, defValue)
        editor.commit()
    }

    fun getLong(context: Context, key: String, defValue: Long) : Long {
        return getLong(context, M_PRIVATE, key, defValue)
    }

    fun getLong(context: Context, mainKey: String, subKey: String, defValue: Long) : Long {
        var defLong : Long
        defLong = defValue

        defLong = get(context, mainKey).getLong(subKey, defValue)
        return defLong
    }

    fun putInt(context: Context, key: String, defValue: Int) {
        putInt(context, M_PRIVATE, key, defValue)
    }

    fun putInt(context: Context, mainKey: String, subKey: String, defValue: Int) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putInt(subKey, defValue)
        editor.commit()
    }

    fun getInt(context: Context, key: String, defValue: Int) : Int? {
        return getInt(context, M_PRIVATE, key, defValue)
    }

    fun getInt(context: Context, mainKey: String, subKey: String, defValue: Int) : Int? {
        val defInt : Int
        defInt = defValue

        var str = get(context, mainKey).getInt(subKey, defValue)
        return str
    }

    fun putMap(context: Context, key : String, strData : Map<String, Any>) {
        putMap(context, key, strData)
    }

    fun putMap(context: Context, mainKey: String, subKey: String, strData: Map<String, Any>) {
        var jsonObjects : JSONObject = JSONObject(strData)
        var jsonStr = jsonObjects.toString()
        putString(context, mainKey, subKey, jsonStr)
    }

    fun getMap(context: Context, key: String) {
        getMap(context, key)
    }

    fun getMap(context: Context, mainKey: String, subKey: String) : Map<String, Any>? {
        var jsonStr = getString(context, mainKey, subKey, "")

        var map : Map<String, Any>? = HashMap<String, Any>()
        map = jsonStr?.let { JsonParser.getMapData(jsonStr) }
        return map
    }
}