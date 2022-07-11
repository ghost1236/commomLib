package net.common.commonlib.extension

import net.common.commonlib.extension.StringExtension.asHashMap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonExtension {

    fun JSONObject.toMap() : Map<String, Any> {
        var map = HashMap<String, Any>()

        var iter = keys()
        while (iter.hasNext()) {
            var key = iter.next()
            var value = get(key)
            if (get(key) is JSONObject) {
                map.put(key, getString(key))
            } else if (get(key) is JSONArray) {
                map.put(key, getJSONArray(key).toArrayList())
            } else {
                map.put(key, getString(key))
            }
        }
        return map
    }

    fun JSONArray.toArrayList() : ArrayList<Map<String, Any>> {
        var list = ArrayList<Map<String, Any>>()

        for (i in 0..length()) {
            try {
                var jsonObject = getJSONObject(i)
                list.add(jsonObject.toString().asHashMap()?.let { it } ?: emptyMap())
            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }

}