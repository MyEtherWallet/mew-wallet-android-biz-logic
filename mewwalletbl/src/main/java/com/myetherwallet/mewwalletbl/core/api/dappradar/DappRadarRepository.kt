package com.myetherwallet.mewwalletbl.core.api.dappradar

import com.myetherwallet.mewwalletbl.core.api.BaseRepository
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.dappradar.DappRadarItem

private const val TAG = "DappRadarRepository"

class DappRadarRepository(private val service: DappRadarApi) : BaseRepository() {

    suspend fun getDapps(page: Int, itemsPerPage: Int): Either<Failure, List<DappRadarItem>> {
        return requestSuspend({ service.getDapps(page, itemsPerPage) }) { it }
    }
}
