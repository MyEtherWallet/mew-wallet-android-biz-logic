package com.myetherwallet.mewwalletbl.core.api.ws

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure

/**
 * Created by BArtWell on 15.02.2022.
 */

abstract class Parser {

    abstract fun <OUT> parse(data: String, clazz: Class<OUT>): Either<Failure.WebSocketError, OUT>
}
