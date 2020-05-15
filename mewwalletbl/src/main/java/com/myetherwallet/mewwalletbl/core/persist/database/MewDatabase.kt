package com.myetherwallet.mewwalletbl.core.persist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myetherwallet.mewwalletbl.core.persist.database.converter.*
import com.myetherwallet.mewwalletbl.core.persist.database.dao.*
import com.myetherwallet.mewwalletbl.data.database.*

/**
 * Created by BArtWell on 17.09.2019.
 */

@Database(
    entities = [
        EntityAccount::class,
        EntityBalance::class,
        EntityNetwork::class,
        EntityPrice::class,
        EntityRecipient::class,
        EntityToken::class,
        EntityTokenDescription::class,
        EntityTransaction::class,
        EntityRecent::class,
        EntityDexToken::class,
        EntityDexPrice::class,
        EntityMarket::class,
        EntitySwap::class,
        EntityWallet::class,
        EntityPurchase::class
    ], version = MewDatabase.DB_VERSION
)
@TypeConverters(
    DateTypeConverter::class,
    BigIntegerConverter::class,
    BigDecimalConverter::class,
    AddressTypeConverter::class,
    TransactionStatusTypeConverter::class,
    PurchaseStateTypeConverter::class
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

    abstract fun getDexTokens(): DexTokensDao

    abstract fun getDexPrices(): DexPricesDao

    abstract fun getMarket(): MarketDao

    abstract fun getExchangeDao(): ExchangeDao

    abstract fun getRecentDao(): RecentDao

    abstract fun getPurchaseDao(): PurchaseDao

    companion object {
        const val DB_NAME = "db_mew"
        const val DB_VERSION = 5
    }
}
