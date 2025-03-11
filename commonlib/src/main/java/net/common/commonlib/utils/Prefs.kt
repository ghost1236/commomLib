package net.common.commonlib.utils

import android.content.Context
import android.content.SharedPreferences
import net.common.commonlib.utils.JsonParser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object Prefs {
    val M_PRIVATE = "private"
    val M_PUBLIC = "public"

    var DEVCIE_ID = "device_id"
    const val COOKIES_STR = "cookies_str"

    private fun get(context: Context, mainKey : String) : SharedPreferences{
        return context.getSharedPreferences(mainKey, Context.MODE_PRIVATE)
    }

    /** StringSet **/
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
    /** StringSet **/

    /** Boolean **/
    fun putBoolean(context: Context, key : String, defValue: Boolean) {
        putBoolean(context, M_PRIVATE, key, defValue)
    }

    fun putBoolean(context: Context, mainKey: String, subKey: String, defValue: Boolean) {
        val editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putBoolean(subKey, defValue)
        editor.commit()
    }

    fun getBoolean(context: Context, key: String, defValue: Boolean) : Boolean {
        return getBoolean(context, M_PRIVATE, key, defValue)
    }

    fun getBoolean(context: Context, mainKey: String, subKey: String, defValue: Boolean) : Boolean {
        return get(context, mainKey).getBoolean(subKey, defValue)
    }
    /** Boolean **/

    /** String **/
    fun putString(context: Context, key: String, defValue: String) {
        putString(context, M_PRIVATE, key, defValue)
    }

    fun putString(context: Context, mainKey: String, subKey: String, defValue: String) {
        val editor : SharedPreferences.Editor = get(context, mainKey).edit()
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
    /** String **/

    /** Long **/
    fun putLong(context: Context, key : String, defValue: Long) {
        putLong(context, key, defValue)
    }

    fun putLong(context: Context, mainKey: String, subKey: String, defValue: Long) {
        val editor : SharedPreferences.Editor = get(context, mainKey).edit()
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
    /** Long **/

    /** Int **/
    fun putInt(context: Context, key: String, defValue: Int) {
        putInt(context, M_PRIVATE, key, defValue)
    }

    fun putInt(context: Context, mainKey: String, subKey: String, defValue: Int) {
        val editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.putInt(subKey, defValue)
        editor.commit()
    }

    fun getInt(context: Context, key: String, defValue: Int) : Int? {
        return getInt(context, M_PRIVATE, key, defValue)
    }

    fun getInt(context: Context, mainKey: String, subKey: String, defValue: Int) : Int? {
        val defInt : Int
        defInt = defValue

        val str = get(context, mainKey).getInt(subKey, defValue)
        return str
    }
    /** Int **/

    /** Map<String, Any> **/
    fun putMap(context: Context, key : String, strData : Map<String, Any>) {
        putMap(context, M_PRIVATE, key, strData)
    }

    fun putMap(context: Context, mainKey: String, subKey: String, strData: Map<String, Any>) {
        val jsonObjects : JSONObject = JSONObject(strData)
        val jsonStr = jsonObjects.toString()
        putString(context, mainKey, subKey, jsonStr)
    }

    fun getMap(context: Context, key: String) : Map<String, Any>? {
        return getMap(context, M_PRIVATE, key)
    }

    fun getMap(context: Context, mainKey: String, subKey: String) : Map<String, Any>? {
        val jsonStr = getString(context, mainKey, subKey, "")

        var map : Map<String, Any>? = HashMap<String, Any>()
        map = jsonStr?.let { JsonParser.getMapData(jsonStr) }
        return map
    }
    /** Map<String, Any> **/

    /** ArrayList<Map<String, Any>> **/
    fun putArrayList(context: Context, key: String, strData: ArrayList<Map<String, Any>>) {
        putArrayList(context, M_PRIVATE, key, strData)
    }

    fun putArrayList(context: Context, mainKey: String, subKey: String, strData: ArrayList<Map<String, Any>>) {
        val jsonArray = JSONArray()
        for (map in strData) {
            val jsonObject = JSONObject(map)
            jsonArray.put(jsonObject)
        }
        val jsonStr = jsonArray.toString()
        putString(context, mainKey, subKey, jsonStr)
    }

    fun getArrayList(context: Context, key: String) : ArrayList<Map<String, Any>>? {
        return getArrayList(context, M_PRIVATE, key)
    }

    fun getArrayList(context: Context, mainKey: String, subKey: String) : ArrayList<Map<String, Any>>? {
        val jsonStr = getString(context, mainKey, subKey, "")
        if (jsonStr.isEmpty()) return null

        return try {
            val jsonArray = JSONArray(jsonStr)
            JsonParser.parse(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }
    /** ArrayList<Map<String, Any>> **/

    fun clear(context: Context) {
        var editor : SharedPreferences.Editor = get(context, Prefs.M_PRIVATE).edit()
        editor.clear()
        editor.commit()
    }

    fun remove(context: Context, subKey: String) {
        var editor : SharedPreferences.Editor = get(context, Prefs.M_PRIVATE).edit()
        editor.remove(subKey)
        editor.commit()
    }

    fun remove(context: Context, mainKey: String, subKey: String) {
        var editor : SharedPreferences.Editor = get(context, mainKey).edit()
        editor.remove(subKey)
        editor.commit()
    }
}