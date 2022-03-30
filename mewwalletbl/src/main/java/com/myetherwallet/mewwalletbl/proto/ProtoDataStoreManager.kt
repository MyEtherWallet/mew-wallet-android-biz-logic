package com.myetherwallet.mewwalletbl.proto

import android.content.Context
import com.myetherwallet.mewwalletbl.RaterData
import com.myetherwallet.mewwalletbl.proto.serializer.RaterDataSerializer

/**
 * Created by BArtWell on 11.03.2022.
 */

private const val FILE_RATER_DATA = "rater_data"

object ProtoDataStoreManager {

    lateinit var raterDataStore: ProtoDataStore<RaterData>
        private set

    internal fun init(context: Context) {
        raterDataStore = ProtoDataStore.create(context, FILE_RATER_DATA, RaterDataSerializer)
    }
}
