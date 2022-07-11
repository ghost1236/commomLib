package net.common.commonlib.extension

import android.util.Base64
import java.nio.ByteBuffer
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object ByteArrayExtension {

    private val ivBytes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

    /**
     * AES256 암호화
     */
    fun ByteArray.toAesEncode(enckey: String) = aesEncodeOrDecode(enckey, Cipher.ENCRYPT_MODE)

    /**
     * AES256 복호화
     */
    fun ByteArray.toAesDecode(enckey: String) = aesEncodeOrDecode(enckey, Cipher.DECRYPT_MODE)


    private fun ByteArray.aesEncodeOrDecode(encKey: String, mode: Int) : ByteArray  {
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(encKey.toByteArray(), "AES")
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(mode, newKey, ivSpec)

        return cipher.doFinal(this)
    }
}