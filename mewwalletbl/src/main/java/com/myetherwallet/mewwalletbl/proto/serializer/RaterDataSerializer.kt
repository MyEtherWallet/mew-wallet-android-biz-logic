package com.myetherwallet.mewwalletbl.proto.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.myetherwallet.mewwalletbl.RaterData
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by BArtWell on 10.03.2022.
 */

object RaterDataSerializer : Serializer<RaterData> {

    override val defaultValue: RaterData = RaterData.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): RaterData {
        try {
            return RaterData.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: RaterData, output: OutputStream) = t.writeTo(output)
}
