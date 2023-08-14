package net.common.commonlib.extension


import android.util.Base64
import net.common.commonlib.extension.ByteArrayExtension.toAesDecode
import net.common.commonlib.extension.ByteArrayExtension.toAesEncode
import net.common.commonlib.extension.JsonExtension.toArrayList
import net.common.commonlib.extension.JsonExtension.toMap
import net.common.commonlib.extension.JsonExtension.toStringArray
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.Normalizer
import java.util.regex.Pattern
import javax.crypto.Cipher

object StringExtension {

    fun String.toAesEncoding(enckey: String) = Base64.encodeToString(toByteArray().toAesEncode(enckey), 0)

    fun String.toAesDecoding(enckey: String) = String(toByteArray().toAesDecode(enckey), Charsets.UTF_8)

    fun String.nullToNum(num : Long) : Long = if (isEmpty() || !isNumber()) num else toLong()

    fun String.nullToNum(num : Int) : Int {
        if (isEmpty() || !isNumber())   return num

        var stringArray : List<String> = split("\\.")
        if (stringArray.isNotEmpty()) {
            stringArray[0]
        }
        return toInt()
    }

    fun String.nullToZero(str : String) : Int = nullToNum(0)

    fun String.nullToZeroL(str : String) : Long = nullToNum(0L)

    fun String.isNumber() : Boolean {
        if (isEmpty())  return false

        var len : Int = trim().length
        var dotCnt = 0
        (0..len).forEach { i ->
            var c = get(i)
            if (i == 0 && (c == '-' || c == '+')) {
            } else if (i == len - 1 && (c == 'f' || c == 'd' || c == 'l')) {
            } else {
                if (c == '.') {
                    dotCnt++
                }
                if (dotCnt > 1) { // 소수점은 1개만 가능
                    return false
                }
                if (c != '.' && (c < 48.digitToChar() || c > 57.digitToChar())) {
                    return false
                }
            }
        }

        return true
    }

    fun String.toBooleanYn() : Boolean = equals("true", true) || equals("yes", true) || equals("y", true)

    fun String.addComma() : String = DecimalFormat("#,###").format(nullToZero(this))

    fun String.isEscapeYn() : Boolean {
        var regex = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*".toRegex()
        if (!normalizeNfkd().matches(regex)) {
            return true
        }
        return false
    }

    fun String.isEmailRegex() : Boolean {
        var regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"
        var p = Pattern.compile(regex)
        var m = p.matcher(normalizeNfkd())
        if (m.matches()) {
            return true
        }
        return false
    }

    fun String.isUrlRegex() : Boolean {
        var regex = "/^(file|gopher|news|nntp|telnet|https?|ftps?|sftp):\\/\\/([a-z0-9-]+\\.)+[a-z0-9]{2,4}.*$/"
        var p = Pattern.compile(regex)
        var m = p.matcher(normalizeNfkd())
        if (m.matches()) {
            return true
        }
        return false
    }

    fun String.isTelRegex() : Boolean {
        var regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$"
        var p = Pattern.compile(regex)
        var m = p.matcher(normalizeNfkd())
        if (m.matches()) {
            return true
        }
        return false
    }

    fun String.isPhoneRegex() : Boolean {
        var regex = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$"
        var p = Pattern.compile(regex)
        var m = p.matcher(normalizeNfkd())
        if (m.matches()) {
            return true
        }
        return false
    }

    fun String.isNumeric() : Boolean = Pattern.matches("^[0-9]*$", this)

    fun String.normalizeNfkd() : String = Normalizer.normalize(this, Normalizer.Form.NFKD)

    fun String.hexToBytes() : ByteArray {
        check(length % 2 != 0) { return byteArrayOf()}
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    fun String.asHashMap() : Map<String, Any>? {
        return try {
            if (isEmpty() || "{}" == this) {
                null
            }

            val jobj = JSONObject(this)
            var iKeys = jobj.keys()
            var map = HashMap<String, Any>()
            while (iKeys.hasNext()) {
                var key = iKeys.next()
                when {
                    jobj.get(key) is JSONObject -> map[key] =  jobj.getJSONObject(key).toMap()
                    jobj.get(key) is JSONArray -> {
                        if (JsonExtension.arrayKeys.contains(key)) {
                            map[key] = jobj.getJSONArray(key).toStringArray()
                        } else {
                            map[key] = jobj.getJSONArray(key).toArrayList()
                        }
                    }
                    else -> {
                        map[key] = jobj.get(key)
                    }
                }
            }
            map
        } catch (e: JSONException) {
            null
        }
    }

}