package com.myetherwallet.mewwalletbl.core.persist.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myetherwallet.mewwalletbl.core.persist.database.dao.*

/**
 * Created by BArtWell on 18.09.2019.
 */

object Database {

    lateinit var instance: MewDatabase

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + AccountsDao.TABLE_NAME + " ADD COLUMN anonymous_id TEXT NOT NULL DEFAULT ''")
            database.execSQL("UPDATE " + AccountsDao.TABLE_NAME + " SET anonymous_id=CAST(RANDOM() AS TEXT)")
        }
    }

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE " + RecentDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, address TEXT NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_recent_address ON " + RecentDao.TABLE_NAME + " (address)")
        }
    }

    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE " + DexTokensDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tokenDescriptionId INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_tokens_tokenDescriptionId ON " + DexTokensDao.TABLE_NAME + " (tokenDescriptionId)")

            database.execSQL("CREATE TABLE " + DexPricesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, dex TEXT NOT NULL, price DOUBLE NOT NULL, base INTEGER NOT NULL, quote INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_dex_base_quote ON " + DexPricesDao.TABLE_NAME + " (dex, base, quote)")

            database.execSQL("CREATE TABLE " + MarketDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, contract TEXT NOT NULL, decimals INTEGER NOT NULL, name TEXT NOT NULL, symbol TEXT NOT NULL, price TEXT NOT NULL, logo TEXT NOT NULL, website TEXT NOT NULL, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_market_contract ON " + MarketDao.TABLE_NAME + " (contract)")

            database.execSQL("CREATE TABLE " + ExchangeDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, txHash TEXT, createTime INTEGER NOT NULL, updateTime INTEGER NOT NULL, fromDescriptionId INTEGER NOT NULL, toDescriptionId INTEGER NOT NULL, fromAmount DOUBLE NOT NULL, toAmount DOUBLE NOT NULL, accountId INTEGER NOT NULL, toAddress TEXT NOT NULL, dex TEXT NOT NULL, status TEXT NOT NULL)")

            database.execSQL("CREATE TABLE " + PurchaseDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, transactionId TEXT NOT NULL, fiatAmount DOUBLE NOT NULL, fiatCurrency TEXT NOT NULL, cryptoAmount DOUBLE NOT NULL, cryptoDescriptionId INTEGER NOT NULL, timestamp INTEGER NOT NULL, status TEXT NOT NULL, provider TEXT NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_purchase_transactionId ON " + PurchaseDao.TABLE_NAME + " (transactionId)")

            dropAndCreateTransactionsDao(database)

            database.execSQL("ALTER TABLE " + AccountsDao.TABLE_NAME + " ADD COLUMN nonce TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            dropAndCreateTransactionsDao(database)

            database.execSQL("DROP TABLE IF EXISTS " + DexPricesDao.TABLE_NAME)
            database.execSQL("CREATE TABLE " + DexPricesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, dex TEXT NOT NULL, price DOUBLE NOT NULL, base INTEGER NOT NULL, quote INTEGER NOT NULL, scale INTEGER NOT NULL, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_dex_base_quote_scale ON " + DexPricesDao.TABLE_NAME + " (dex, base, quote, scale)")
        }
    }

    private fun dropAndCreateTransactionsDao(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS " + TransactionsDao.TABLE_NAME)
        database.execSQL("CREATE TABLE " + TransactionsDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, accountId INTEGER NOT NULL, fromRecipientId INTEGER NOT NULL, toRecipientId INTEGER NOT NULL, tokenDescriptionId INTEGER NOT NULL, amount DOUBLE NOT NULL, status TEXT NOT NULL, timestamp INTEGER NOT NULL, txHash TEXT NOT NULL)")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_transactions_txHash_tokenDescriptionId_fromRecipientId_toRecipientId ON " + TransactionsDao.TABLE_NAME + " (txHash, tokenDescriptionId, fromRecipientId, toRecipientId)")
    }

    internal fun init(context: Context) {
        instance = Room
            .databaseBuilder(context, MewDatabase::class.java, MewDatabase.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .addMigrations(MIGRATION_3_4)
            .addMigrations(MIGRATION_4_5)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    fun drop(context: Context) {
        context.deleteDatabase(MewDatabase.DB_NAME)
        init(context)
    }
}
