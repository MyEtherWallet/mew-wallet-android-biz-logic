package com.myetherwallet.mewwalletbl.core.persist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myetherwallet.mewwalletbl.core.persist.database.converter.AddressTypeConverter
import com.myetherwallet.mewwalletbl.core.persist.database.converter.BigIntegerConverter
import com.myetherwallet.mewwalletbl.core.persist.database.converter.DateTypeConverter
import com.myetherwallet.mewwalletbl.core.persist.database.converter.TransactionStatusTypeConverter
import com.myetherwallet.mewwalletbl.core.persist.database.dao.*
import com.myetherwallet.mewwalletbl.data.database.*

/**
 * Created by BArtWell on 17.09.2019.
 */

private const val VERSION = 1

@Database(
    entities = [
        EntityAccount::class,
        EntityBalance::class,
        EntityContact::class,
        EntityNetwork::class,
        EntityPrice::class,
        EntityRecipient::class,
        EntityToken::class,
        EntityTokenDescription::class,
        EntityTransaction::class,
        EntityWallet::class
    ], version = VERSION
)
@TypeConverters(
    DateTypeConverter::class,
    BigIntegerConverter::class,
    AddressTypeConverter::class,
    TransactionStatusTypeConverter::class
)
abstract class MewDatabase : RoomDatabase() {

    abstract fun getWalletsDao(): WalletsDao

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getNetworkDao(): NetworksDao

    abstract fun getRecipientDao(): RecipientDao

    abstract fun getTokensDao(): TokensDao

    abstract fun getTokenDescriptionDao(): TokenDescriptionDao

    abstract fun getBalancesDao(): BalancesDao

    abstract fun getPricesDao(): PricesDao

    abstract fun getTransactionsDao(): TransactionsDao
}
