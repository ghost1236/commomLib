package net.common.commonlib.extension

import net.common.commonlib.extension.StringExtension.toBooleanYn
import org.json.JSONException
import org.json.JSONObject

object MapExtension {

    fun Map<String, Any>.asString(key: String) : String? = get(key)?.toString()

    fun Map<String, Any>.asInt(key: String) : Int? = get(key)?.toString()?.toIntOrNull()

    fun Map<String, Any>.asLong(key: String) : Long? = get(key)?.toString()?.toLongOrNull()

    fun Map<String, Any>.asBoolean(key: String) : Boolean? = get(key)?.toString()?.toBooleanYn()

    fun Map<String, Any>.asHashMap(key: String) : Map<String, Any>? = get(key)?.let { it as? Map<String, Any> } ?: null

    fun Map<String, Any>.asArrayList(key: String) : ArrayList<Map<String, Any>>? = get(key)?.let { it as? ArrayList<Map<String, Any>> } ?: null

    /**
     * Map -> JSONObject
     */
    fun Map<String, Any>.asJSONObject() : JSONObject {
        return try {
            val jsonObject = JSONObject()

            for (entry in entries) {
                jsonObject.put(entry.key, entry.value)
            }
            jsonObject
        } catch (e: JSONException) {
            e.printStackTrace()
            JSONObject()
        }
    }
}