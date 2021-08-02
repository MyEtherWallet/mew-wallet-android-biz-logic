package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.analytics.AnalyticsApiRepository
import com.myetherwallet.mewwalletbl.core.api.dappradar.DappRadarRepository
import com.myetherwallet.mewwalletbl.core.api.mew.MewApiRepository
import com.myetherwallet.mewwalletbl.core.api.node.EstimatedGasApiRepository
import com.myetherwallet.mewwalletbl.core.api.node.NodeApiRepository
import com.myetherwallet.mewwalletbl.core.api.wyre.WyreApiRepository
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import kotlinx.coroutines.*

/**
 * Created by BArtWell on 13.09.2019.
 */
private const val TAG = "BaseInteractor"

abstract class BaseInteractor<out Type, in Params>(private val viewModelScope: CoroutineScope? = null) where Type : Any {

    val nodeApiRepository by lazy { NodeApiRepository(ApiManager.getNodeApi()) }
    val mewApiRepository by lazy { MewApiRepository(ApiManager.getMewApi()) }
    val analyticsApiRepository by lazy { AnalyticsApiRepository(ApiManager.getAnalyticsApi()) }
    val wyreApiRepository by lazy { WyreApiRepository(ApiManager.getWyreApi()) }
    val estimatedGasApiRepository by lazy { EstimatedGasApiRepository(ApiManager.getEstimatedGasApi()) }
    val dappRadarApiRepository by lazy { DappRadarRepository(ApiManager.getDappRadarApi()) }

    abstract suspend fun run(params: Params): Either<Failure, Type>

    fun execute(params: Params, onResult: (Either<Failure, Type>) -> Unit) {
        val job = GlobalScope.async {
            try {
                run(params)
            } catch (e: Exception) {
                if (Database.isDatabaseRemovingInProgress) {
                    MewLog.d(TAG, "Can't call run", e)
                    Either.Left(Failure.UnknownError(e))
                } else {
                    throw e
                }
            }
        }
        val scope = viewModelScope ?: GlobalScope
        scope.launch(Dispatchers.IO) { onResult.invoke(job.await()) }
    }

    fun executeSync(params: Params): Either<Failure, Type> {
        return runBlocking {
            run(params)
        }
    }

    protected fun runOnUiThread(callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            callback.invoke()
        }
    }

    class None
}
