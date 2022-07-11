package net.common.commonlib.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object JsonParser {

    /**
     * Json String -> Map<String, Any>
     */
    fun getMapData(str : String) : Map<String, Any> {
        if (str.isEmpty() || "{}".equals(str)) {
            return HashMap<String, Any>()
        }

        try {
            return getJsonData(str)
        } catch (e : JSONException) {
            e.printStackTrace()
        }
        return HashMap<String, Any>()
    }

    /**
     * JSONObject -> Map<String, Any>
     */
    fun parse(jobj : JSONObject) : Map<String, Any> {
        var map = HashMap<String, Any>()
        var iter = jobj.keys()
        while (iter.hasNext()) {
            var key = iter.next()
            var value = jobj.get(key)
            if (jobj.get(key) is JSONObject) {
                map.put(key, jobj.getString(key))
            } else if (jobj.get(key) is JSONArray) {
                map.put(key, parse(jobj.getJSONArray(key)))
            } else {
                map.put(key, jobj.getString(key))
            }
        }
        return map
    }

    /**
     * JSONArray -> ArrayList<Map<String, Any>
     */
    fun parse(jarr : JSONArray) : ArrayList<Map<String, Any>> {
        var list = ArrayList<Map<String, Any>>()
        var len = jarr.length()
        for (i in 0..len) {
            try {
                var j = jarr.getJSONObject(i)
                list.add(getJsonData(j.toString()))
            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }
        return list
    }

    /**
     * Json String -> Map<String, Any>
     */
    fun getJsonData(data : String) : Map<String, Any> {
        try {
            var jobj = JSONObject(data)
            var iKeys = jobj.keys()
            var map = HashMap<String, Any>()
            while (iKeys.hasNext()) {
                var key = iKeys.next()
                if (jobj.get(key) is JSONObject) {
                    map.put(key, parse(jobj.getJSONObject(key)))
                } else if (jobj.get(key) is JSONArray) {
                    map.put(key, parse(jobj.getJSONArray(key)))
                } else {
                    map.put(key, jobj.get(key))
                }
            }
            return map
        } catch (e : JSONException) {
            e.printStackTrace()
        }
        return HashMap<String, Any>()
    }
}