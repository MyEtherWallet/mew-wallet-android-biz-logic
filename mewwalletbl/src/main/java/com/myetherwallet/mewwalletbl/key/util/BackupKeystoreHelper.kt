package com.myetherwallet.mewwalletbl.key.util

import android.content.Context
import com.myetherwallet.mewwalletbl.key.BaseEncryptHelper
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.storage.EncryptStorage

/**
 * Created by BArtWell on 29.07.2020.
 */

class BackupKeystoreHelper(
    context: Context,
    private val index: Int,
    private val encryptStorage: EncryptStorage
) : KeystoreHelper(
    context,
    KeyType.PIN,
    encryptStorage,
    "MewWalletKeyBackup$index"
) {

    constructor(encryptHelper: BaseEncryptHelper, index: Int) : this(encryptHelper.context, index, encryptHelper.encryptStorage)

    override fun saveIv(iv: ByteArray) {
        encryptStorage.addKeystoreIvBackup(index, iv)
    }

    override fun getIv() = encryptStorage.getKeystoreIvBackup(index)
}
