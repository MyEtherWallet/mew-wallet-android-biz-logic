package com.myetherwallet.mewwalletbl.data

/**
 * Created by BArtWell on 25.07.2019.
 */

class SocketMessage private constructor(
    val connId: String,
    val signed: String?,
    val version: EncryptedMessage?,
    val data: EncryptedMessage?
) {
    constructor(connId: String, signed: String, version: EncryptedMessage) : this(connId, signed, version, null)

    constructor(connId: String, data: EncryptedMessage) : this(connId, null, null, data)
}
