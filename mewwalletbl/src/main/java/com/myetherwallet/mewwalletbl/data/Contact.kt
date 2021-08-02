package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.myetherwallet.mewwalletbl.data.database.Recent
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class Contact constructor(
    var address: Address?,
    var ens: String? = null,
    var name: String? = null
) : Parcelable {

    @IgnoredOnParcel
    val isEnsEnabled = address == null

    fun getAddressOrEns() = if (isEnsEnabled) ens!! else address?.address!!

    companion object {
        fun create(addressOrEns: String, name: String? = null): Contact? {
            var address: Address? = null
            var ens: String? = null
            val addressObject = Address.createEthereum(addressOrEns)
            when {
                addressObject != null -> {
                    address = addressObject
                }
                checkEns(addressOrEns) -> {
                    ens = addressOrEns
                }
                else -> {
                    return null
                }
            }
            return Contact(address, ens, name)
        }

        private fun checkEns(ens: String) = false // "^[0-9a-z][0-9a-z.-]+[0-9a-z]\\.eth$".toRegex() matches ens

        fun fromRecent(recent: List<Recent>): List<Contact> = recent.map { create(it.address.address, it.name)!! }
    }
}
