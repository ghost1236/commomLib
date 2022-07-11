package net.common.commonlib.extension

import android.annotation.SuppressLint
import android.database.Cursor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object CursorExtension {

    fun Cursor.cursortoJsonArray() : JSONArray {
        var resultSet = JSONArray()
        moveToFirst()
        while (!isAfterLast) {
            var totalColumn = columnCount
            var rowObject = JSONObject()

            (0..columnCount).forEach { i ->
                columnNames[i]?.let {
                    try {
                        rowObject[it] to getString(i)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            resultSet.put(rowObject)
            moveToNext()
        }
        close()
        return resultSet
    }

    fun Cursor.cursorToJsonObject() : JSONObject {
        var retVal = JSONObject()
        (0..columnCount).forEach {
            var cName = columnNames[it]
            try {
                retVal[cName] to getString(it)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return retVal
    }

    @SuppressLint("Range")
    fun Cursor.cursorToMap() : Map<String, Any> {
        var map = HashMap<String, Any>();

        (0..columnCount).forEach {
            var columnName = columnNames[it]
            var cursorType = getType(it)

            when (cursorType) {
                Cursor.FIELD_TYPE_NULL -> {
                    map[columnName] = ""
                }
                Cursor.FIELD_TYPE_INTEGER -> {
                    map[columnName] = getLong(getColumnIndex(columnName))
                }
                Cursor.FIELD_TYPE_FLOAT -> {
                    map[columnName] = getFloat(getColumnIndex(columnName))
                }
                Cursor.FIELD_TYPE_STRING -> {
                    map[columnName] = getString(getColumnIndex(columnName))
                }
                Cursor.FIELD_TYPE_BLOB -> {
                    map[columnName] = getBlob(getColumnIndex(columnName))
                }
                else -> {
                    map[columnName] = ""
                }
            }
        }

        return map
    }

    fun Cursor.cursorToArray() : ArrayList<Map<String, Any>> {
        var list = ArrayList<Map<String, Any>>()
        this?.let {
            moveToPosition(-1)
            while (moveToNext()) {
                list.add(cursorToMap())
            }
        }
        return list;
    }



}