package com.example.survey2

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.*


public class ObjectSerializer {
    companion object {
        public fun serialize(obj: Any?) : String {
            if (obj == null) {
                return ""
            }

            var baos = ByteArrayOutputStream()
            var oos = ObjectOutputStream(baos)
            oos.writeObject(obj)
            oos.close()

            return encodeBytes(baos.toByteArray())
        }

        public fun deserialize(str: String?) : Any? {
            if (str == null || str.length == 0) {
                return null
            }

            var bais = ByteArrayInputStream(decodeBytes(str))
            var ois = ObjectInputStream(bais)

            return ois.readObject()
        }

        private fun encodeBytes(bytes: ByteArray) : String {
            var buffer = StringBuffer()

            for (byte in bytes) {
                buffer.append(((byte.toInt() shr 4) and 0xF + 'a'.code).toChar())
                buffer.append(((byte.toInt()) and 0xF + 'a'.code).toChar())
            }

            return buffer.toString()
        }

        private fun decodeBytes(str: String) : ByteArray {
            val bytes = ByteArray(str.length / 2)

            for (i in 0..(str.length - 2) step 2) {
                var c1 = str[i]
                var c2 = str[i + 1]
                val value = ((c1 - 'a').toInt() shl 4) or (c2 - 'a').toInt()
                bytes[i / 2] = value.toByte()
            }

            return bytes
        }
    }
}