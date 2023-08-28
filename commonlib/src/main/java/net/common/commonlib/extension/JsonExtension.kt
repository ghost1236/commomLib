package net.common.commonlib.extension

import net.common.commonlib.extension.StringExtension.asHashMap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonExtension {

    var arrayKeys = arrayListOf<String>("healthIssue", "ingredientsList" , "feedfunctionList", "feedHealthissueList", "feedingGuideList")

    fun JSONObject.toMap() : Map<String, Any> {
        var map = HashMap<String, Any>()

        var iter = keys()
        while (iter.hasNext()) {
            var key = iter.next()
            var value = get(key)
            if (value is JSONObject) {
                map[key] = getJSONObject(key).toMap()
            } else if (value is JSONArray) {
                if (arrayKeys.contains(key)) {
                    map[key] = getJSONArray(key).toStringArray()
                } else {
                    map[key] = getJSONArray(key).toArrayList()
                }
            } else {
                map[key] = getString(key)
            }
        }
        return map
    }

    fun JSONArray.toArrayList() : ArrayList<Map<String, Any>> {
        var list = ArrayList<Map<String, Any>>()

        for (i in 0 until length()) {
            try {
                var jsonObject = getJSONObject(i)
                list.add(jsonObject.toString().asHashMap()?.let { it } ?: emptyMap())
            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }

    fun JSONArray.toStringArray() : ArrayList<String> {
        var list = ArrayList<String>()

        for (i in 0 until length()) {
            try {
                var jsonObject = getString(i)
                list.add(jsonObject.toString())
            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }
}