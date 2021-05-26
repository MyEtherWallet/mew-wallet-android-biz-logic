package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.StakedHistoryDao
import com.myetherwallet.mewwalletbl.data.staked.StakedHistoryInfo
import com.myetherwallet.mewwalletbl.data.staked.StakedStatus
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import java.math.BigDecimal
import java.util.*

@Entity(tableName = StakedHistoryDao.TABLE_NAME, indices = [Index(value = ["tx_hash"], unique = true)])
data class EntityStakedInfo(
    val address: Address,
    @ColumnInfo(name = "request_uuid")
    val provisioningRequestUuid: String,
    val price: BigDecimal,
    val status: StakedStatus,
    val amount: BigDecimal,
    val balance: BigDecimal,
    val earned: BigDecimal? = null,
    @ColumnInfo(name = "eth2_address")
    val eth2Address: String? = null,
    @ColumnInfo(name = "tx_hash")
    val txHash: String,
    val apr: BigDecimal? = null,
    @ColumnInfo(name = "current_apr")
    val currentApr: BigDecimal? = null,
    @ColumnInfo(name = "average_apr")
    val averageApr: BigDecimal? = null,
    val timestamp: Date,
    @ColumnInfo(name = "estimated_timestamp")
    val estimatedTimestamp: Date? = null,
    @ColumnInfo(name = "queue_position")
    val queuePosition: Int? = null,
    @ColumnInfo(name = "queue_total")
    val queueTotal: Int? = null,
    @ColumnInfo(name = "eth_two_exited")
    val amountExited: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "eth_two_addresses_exited")
    val eth2AddressExited: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {

        fun fromStakedHistoryInfo(data: StakedHistoryInfo): EntityStakedInfo? {
            return data.txHash?.let {
                EntityStakedInfo(
                    data.address,
                    data.provisioningRequestUuid,
                    data.price,
                    data.status ?: StakedStatus.CREATED,
                    data.amount.hexToBigInteger().toTokenValue(9),
                    data.balance.hexToBigInteger().toTokenValue(9),
                    data.earned.hexToBigInteger().toTokenValue(9),
                    data.eth2Address.joinToString(","),
                    data.txHash,
                    data.apr?.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    data.currentApr.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    data.averageApr.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    data.timestamp,
                    data.queue?.estimatedActivationTimestamp,
                    data.queue?.position,
                    data.queue?.total,
                    data.amountExited.hexToBigInteger().toTokenValue(9),
                    if (data.eth2AddressExited.isNullOrEmpty()) null else data.eth2AddressExited.joinToString(",")
                )
            }
        }
    }
}
