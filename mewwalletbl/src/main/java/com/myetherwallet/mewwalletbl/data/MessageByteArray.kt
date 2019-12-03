package com.myetherwallet.mewwalletbl.data

/**
 * Created by BArtWell on 13.08.2019.
 */

data class MessageByteArray(
    val data: ByteArray
) {

    val type = "Buffer"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageByteArray

        if (!data.contentEquals(other.data)) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}