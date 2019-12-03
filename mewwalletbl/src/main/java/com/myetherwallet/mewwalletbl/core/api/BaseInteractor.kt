package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.api.mew.MewApiRepository
import com.myetherwallet.mewwalletbl.core.api.node.NodeApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Created by BArtWell on 13.09.2019.
 */

abstract class BaseInteractor<out Type, in Params> where Type : Any {

    val nodeApiRepository by lazy { NodeApiRepository(ApiFactory.getNodeApi()) }
    val mewApiRepository by lazy { MewApiRepository(ApiFactory.getMewApi()) }

    abstract suspend fun run(params: Params): Either<Failure, Type>

    fun execute(params: Params, onResult: (Either<Failure, Type>) -> Unit) {
        val job = GlobalScope.async { run(params) }
        GlobalScope.launch(Dispatchers.Main) { onResult.invoke(job.await()) }
    }

    class None
}
