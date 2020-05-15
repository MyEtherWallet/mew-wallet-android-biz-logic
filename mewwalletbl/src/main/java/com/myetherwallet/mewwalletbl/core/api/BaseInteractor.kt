package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.api.analytics.AnalyticsApiRepository
import com.myetherwallet.mewwalletbl.core.api.mew.MewApiRepository
import com.myetherwallet.mewwalletbl.core.api.node.NodeApiRepository
import com.myetherwallet.mewwalletbl.core.api.dex.DexApiRepository
import kotlinx.coroutines.*

/**
 * Created by BArtWell on 13.09.2019.
 */

abstract class BaseInteractor<out Type, in Params> where Type : Any {

    val nodeApiRepository by lazy { NodeApiRepository(ApiManager.getNodeApi()) }
    val mewApiRepository by lazy { MewApiRepository(ApiManager.getMewApi()) }
    val dexApiRepository by lazy { DexApiRepository(ApiManager.getDexApi()) }
    val analyticsApiRepository by lazy { AnalyticsApiRepository(ApiManager.getAnalyticsApi()) }

    abstract suspend fun run(params: Params): Either<Failure, Type>

    fun execute(params: Params, onResult: (Either<Failure, Type>) -> Unit) {
        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult.invoke(job.await()) }
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
