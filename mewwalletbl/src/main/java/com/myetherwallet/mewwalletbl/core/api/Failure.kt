package com.myetherwallet.mewwalletbl.core.api

/**
 * Created by BArtWell on 13.09.2019.
 */

sealed class Failure(val throwable: Throwable?) {
    class NetworkConnection : Failure(null)
    class ServerError(throwable: Throwable) : Failure(throwable)
    class WrongArguments : Failure(null)
    class CommonError(errorCode: Int, errorMessage: String) : Failure(CommonErrorThrowable(errorCode, errorMessage))
    class UnknownError(throwable: Throwable) : Failure(throwable)

    class CommonErrorThrowable(val errorCode: Int, val errorMessage: String) : Throwable()

    abstract class FeatureFailure(throwable: Throwable) : Failure(throwable)
}
