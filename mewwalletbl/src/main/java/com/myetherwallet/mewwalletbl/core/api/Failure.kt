package com.myetherwallet.mewwalletbl.core.api

/**
 * Created by BArtWell on 13.09.2019.
 */

sealed class Failure(val throwable: Throwable?) {
    class NetworkConnection : Failure(null)
    class ServerError(throwable: Throwable) : Failure(throwable)
    class WrongArguments : Failure(null)
    class UnknownError(throwable: Throwable) : Failure(throwable)

    abstract class FeatureFailure(throwable: Throwable) : Failure(throwable)
}
