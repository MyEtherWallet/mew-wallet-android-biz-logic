package com.myetherwallet.mewwalletbl.core.persist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myetherwallet.mewwalletbl.core.persist.database.converter.*
import com.myetherwallet.mewwalletbl.core.persist.database.dao.*
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnBalance
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
        EntityDexPrice::class,
        EntitySwap::class,
        EntityWallet::class,
        EntityPurchase::class,
        EntityPriceHistory::class,
        EntityStakedInfo::class,
        EntityDapp::class,
        EntityBrowserFavorite::class,
        EntityBrowserRecent::class,
        EntityDappRadar::class,
        EntityExchangeRates::class,
        EntityBinanceHistory::class,
        EntityLocalTransaction::class,
        EntitySwapToken::class,
        EntityYearnHistory::class,
        EntityYearnBalance::class
    ], version = MewDatabase.DB_VERSION
)
@TypeConverters(
    DateTypeConverter::class,
    BigIntegerConverter::class,
    BigDecimalConverter::class,
    AddressTypeConverter::class,
    TransactionStatusTypeConverter::class,
    PurchaseStateTypeConverter::class,
    StakedStatusConverter::class,
    MoveStatusConverter::class,
    YearnTypeConverter::class
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

    abstract fun getDexPrices(): DexPricesDao

    abstract fun getExchangeDao(): ExchangeDao

    abstract fun getRecentDao(): RecentDao

    abstract fun getPurchaseDao(): PurchaseDao

    abstract fun getPriceHistoryDao(): PriceHistoryDao

    abstract fun getStakedHistoryDao(): StakedHistoryDao

    abstract fun getDappDao(): DappDao

    abstract fun getBrowserFavoriteDao(): BrowserFavoriteDao

    abstract fun getBrowserRecentDao(): BrowserRecentDao

    abstract fun getDappRadarDao(): DappRadarDao

    abstract fun getCurrencyDao(): CurrencyDao

    abstract fun getBinanceHistoryDao(): BinanceHistoryDao

    abstract fun getLocalTransactionsDao(): LocalTransactionsDao

    abstract fun getSwapTokensDao(): SwapTokensDao

    abstract fun getYearnHistoryDao(): YearnHistoryDao

    abstract fun getYearnBalanceDao(): YearnBalanceDao

    companion object {
        const val DB_NAME = "db_mew"
        const val DB_VERSION = 20
    }
}
