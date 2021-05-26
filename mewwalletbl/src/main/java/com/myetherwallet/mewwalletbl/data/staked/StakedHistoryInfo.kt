package com.myetherwallet.mewwalletbl.data.staked

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
class StakedHistoryInfo(
    @SerializedName("address")
    val address: Address,
    @SerializedName("provisioning_request_uuid")
    val provisioningRequestUuid: String,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("status")
    val status: StakedStatus?,
    @SerializedName("eth_two_staked")
    val amount: String,
    @SerializedName("eth_two_balance")
    val balance: String,
    @SerializedName("eth_two_earned")
    val earned: String,
    @SerializedName("eth_two_addresses")
    val eth2Address: List<String>,
    @SerializedName("eth_two_exited")
    val amountExited: String,
    @SerializedName("eth_two_addresses_exited")
    val eth2AddressExited: List<String>,
    @SerializedName("hash")
    val txHash: String?,
    @SerializedName("apr")
    val apr: String?,
    @SerializedName("current_apr")
    val currentApr: String,
    @SerializedName("average_apr")
    val averageApr: String,
    @SerializedName("queue")
    val queue: Queue?
) : Parcelable {

    @Parcelize
    class Queue(
        @SerializedName("position")
        val position: Int,
        @SerializedName("total")
        val total: Int,
        @SerializedName("estimated_activation_timestamp")
        val estimatedActivationTimestamp: Date
    ) : Parcelable
}