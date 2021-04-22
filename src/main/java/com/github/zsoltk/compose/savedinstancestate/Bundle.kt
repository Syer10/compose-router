package com.github.zsoltk.compose.savedinstancestate

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

data class Bundle(private val items: MutableMap<String, Any> = mutableMapOf()) {
    private fun put(key: String, item: Any) {
        items[key] = item
    }

    operator fun get(key: String) = items[key]

    private inline fun <reified T> getTyped(key: String): T? {
        return items[key] as? T
    }

    private inline fun <reified T> getTyped(key: String, defaultValue: T): T {
        return getTyped(key) ?: defaultValue
    }


    fun remove(key: String) {
        items.remove(key)
    }

    fun size() = items.size

    fun clear() = items.clear()

    fun containsKey(key: String) = items.containsKey(key)

    fun putAll(bundle: Bundle) {
        items.putAll(bundle.keySet().mapNotNull { key -> bundle[key]?.let { key to it } })
    }

    fun keySet() = items.keys.toSet()

    fun isEmpty() = items.isEmpty()

    fun getBoolean(key: String) = getTyped<Boolean>(key) ?: false

    fun getBoolean(key: String, defaultValue: Boolean) = getTyped(key, defaultValue)

    fun putBoolean(key: String, item: Boolean) {
        put(key, item)
    }

    fun getBooleanArray(key: String) = getTyped<BooleanArray>(key)

    fun putBooleanArray(key: String, item: BooleanArray) {
        put(key, item)
    }

    fun getBundle(key: String) = getTyped<Bundle>(key)

    fun putBundle(key: String, item: Bundle) {
        put(key, item)
    }

    fun getByte(key: String) = getTyped<Byte>(key) ?: 0.toByte()

    fun getByte(key: String, defaultValue: Byte) = getTyped(key, defaultValue)

    fun putByte(key: String, item: Byte) {
        put(key, item)
    }

    fun getByteArray(key: String) = getTyped<ByteArray>(key)

    fun putByteArray(key: String, item: ByteArray) {
        put(key, item)
    }

    fun getChar(key: String) = getTyped<Char>(key) ?: 0.toChar()

    fun getChar(key: String, defaultValue: Char) = getTyped(key, defaultValue)

    fun putChar(key: String, item: Char) {
        put(key, item)
    }

    fun getCharArray(key: String) = getTyped<CharArray>(key)

    fun putCharArray(key: String, item: CharArray) {
        put(key, item)
    }

    fun getCharSequence(key: String) = getTyped<CharSequence>(key)

    fun putCharSequence(key: String, item: CharSequence) {
        put(key, item)
    }

    fun getCharSequenceArray(key: String) = getTyped<Array<CharSequence?>>(key)

    fun putCharSequenceArray(key: String, item: Array<CharSequence?>) {
        put(key, item)
    }

    fun getCharSequenceArrayList(key: String) = getTyped<List<CharSequence?>>(key)

    fun putCharSequenceArrayList(key: String, item: List<CharSequence?>) {
        put(key, item)
    }

    fun getDouble(key: String) = getTyped<Double>(key) ?: 0.0

    fun getDouble(key: String, defaultValue: Double) = getTyped(key, defaultValue)

    fun putDouble(key: String, item: Double) {
        put(key, item)
    }

    fun getDoubleArray(key: String) = getTyped<DoubleArray>(key)

    fun putDoubleArray(key: String, item: DoubleArray) {
        put(key, item)
    }

    fun getFloat(key: String) = getTyped<Float>(key) ?: 0.0F

    fun getFloat(key: String, defaultValue: Float) = getTyped(key, defaultValue)

    fun putFloat(key: String, item: Float) {
        put(key, item)
    }

    fun getFloatArray(key: String) = getTyped<FloatArray>(key)

    fun putFloatArray(key: String, item: FloatArray) {
        put(key, item)
    }

    fun getInt(key: String) = getTyped<Int>(key) ?: 0

    fun getInt(key: String, defaultValue: Int) = getTyped(key, defaultValue)

    fun putInt(key: String, item: Int) {
        put(key, item)
    }

    fun getIntArray(key: String) = getTyped<IntArray>(key)

    fun putIntArray(key: String, item: IntArray) {
        put(key, item)
    }

    fun getIntegerArrayList(key: String) = getTyped<List<Int?>>(key)

    fun putIntegerArrayList(key: String, item: List<Int?>) {
        put(key, item)
    }

    fun getLong(key: String) = getTyped<Long>(key) ?: 0L

    fun getLong(key: String, defaultValue: Long) = getTyped(key, defaultValue)

    fun putLong(key: String, item: Long) {
        put(key, item)
    }

    fun getLongArray(key: String) = getTyped<LongArray>(key)

    fun putLongArray(key: String, item: LongArray) {
        put(key, item)
    }

    fun getString(key: String) = getTyped<String>(key)

    fun getString(key: String, defaultValue: String) = getTyped<String>(key) ?: defaultValue

    fun putString(key: String, item: String) {
        put(key, item)
    }

    fun getStringArray(key: String) = getTyped<Array<String?>>(key)

    fun putStringArray(key: String, item: Array<String>) {
        put(key, item)
    }

    fun getShort(key: String) = getTyped<Short>(key) ?: 0

    fun getShort(key: String, defaultValue: Short) = getTyped(key, defaultValue)

    fun putShort(key: String, item: Short) {
        put(key, item)
    }

    fun getShortArray(key: String) = getTyped<ShortArray>(key)

    fun putShortArray(key: String, item: ShortArray) {
        put(key, item)
    }

    fun putSerializable(key: String, serializable: Serializable) {
        ByteArrayOutputStream().use { outStream ->
            ObjectOutputStream(outStream).use {
                it.writeObject(serializable)
            }
            put(key, outStream.toByteArray())
        }
    }

    fun getSerializable(key: String) = getTyped<ByteArray>(key)?.let { bytes ->
        bytes.inputStream().use { inStream ->
            ObjectInputStream(inStream).use {
                it.readObject() as? Serializable
            }
        }
    }

    fun getSerializable(key: String, defaultValue: Serializable) = getSerializable(key) ?: defaultValue
}