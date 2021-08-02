package com.myetherwallet.mewwalletbl.core.persist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.myetherwallet.mewwalletbl.core.persist.database.dao.*
import com.myetherwallet.mewwalletbl.data.Blockchain

/**
 * Created by BArtWell on 18.09.2019.
 */

object Database {

    var isDatabaseRemovingInProgress = false
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
            database.execSQL("CREATE TABLE dex_tokens (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tokenDescriptionId INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_tokens_tokenDescriptionId ON dex_tokens (tokenDescriptionId)")

            database.execSQL("CREATE TABLE " + DexPricesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, dex TEXT NOT NULL, price DOUBLE NOT NULL, base INTEGER NOT NULL, quote INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_dex_base_quote ON " + DexPricesDao.TABLE_NAME + " (dex, base, quote)")

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

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            dropAndCreateTransactionsDao(database)

            database.execSQL("DROP TABLE IF EXISTS " + DexPricesDao.TABLE_NAME)
            database.execSQL("CREATE TABLE " + DexPricesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, dex TEXT NOT NULL, price DOUBLE NOT NULL, base INTEGER NOT NULL, quote INTEGER NOT NULL, scale INTEGER NOT NULL, timestamp INTEGER NOT NULL, marketImpact DOUBLE NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_dex_base_quote_scale ON " + DexPricesDao.TABLE_NAME + " (dex, base, quote, scale)")

            database.execSQL("DROP TABLE IF EXISTS " + BalancesDao.TABLE_NAME)
            database.execSQL("CREATE TABLE " + BalancesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, tokenId INTEGER NOT NULL, amount TEXT NOT NULL, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_tokenId_timestamp ON " + BalancesDao.TABLE_NAME + " (tokenId, timestamp)")
        }
    }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS market")
            database.execSQL("CREATE TABLE " + PriceHistoryDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, contract TEXT NOT NULL, interval TEXT NOT NULL, price DOUBLE NOT NULL, volume DOUBLE NOT NULL, timestamp INTEGER NOT NULL)")
            database.execSQL("ALTER TABLE " + PricesDao.TABLE_NAME + " ADD COLUMN sparkline TEXT NOT NULL DEFAULT ''")

            database.execSQL("DROP TABLE IF EXISTS " + DexPricesDao.TABLE_NAME)
            database.execSQL("CREATE TABLE " + DexPricesDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, exchange TEXT NOT NULL, dex TEXT NOT NULL, price DOUBLE NOT NULL, base INTEGER NOT NULL, quote INTEGER NOT NULL, scale INTEGER NOT NULL, timestamp INTEGER NOT NULL, marketImpact DOUBLE NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_exchange_dex_base_quote_scale ON " + DexPricesDao.TABLE_NAME + " (exchange, dex, base, quote, scale)")
        }
    }

    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + TokensDao.TABLE_NAME + " ADD COLUMN isHidden INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + AccountsDao.TABLE_NAME + " ADD COLUMN eth2_address TEXT NOT NULL DEFAULT ''")
            database.execSQL("CREATE TABLE " + StakedHistoryDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, address TEXT NOT NULL, request_uuid TEXT NOT NULL, price DOUBLE NOT NULL, status TEXT NOT NULL, amount DOUBLE NOT NULL, balance DOUBLE NOT NULL, earned DOUBLE, eth2_address TEXT, tx_hash TEXT NOT NULL, apr DOUBLE, current_apr DOUBLE, average_apr DOUBLE, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_" + StakedHistoryDao.TABLE_NAME + "_tx_hash ON " + StakedHistoryDao.TABLE_NAME + " (tx_hash)")
        }
    }

    private val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + StakedHistoryDao.TABLE_NAME + " ADD COLUMN estimated_timestamp INTEGER")
            database.execSQL("ALTER TABLE " + StakedHistoryDao.TABLE_NAME + " ADD COLUMN queue_position INTEGER")
            database.execSQL("ALTER TABLE " + StakedHistoryDao.TABLE_NAME + " ADD COLUMN queue_total INTEGER")
        }
    }

    private val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE dex_tokens ADD COLUMN volume_24h DOUBLE NOT NULL DEFAULT 0")

            database.execSQL("CREATE TABLE " + DappDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, url TEXT NOT NULL DEFAULT '', name TEXT NOT NULL DEFAULT '', desc TEXT NOT NULL DEFAULT '', img TEXT NOT NULL DEFAULT '', category TEXT NOT NULL DEFAULT '')")
            database.execSQL("CREATE UNIQUE INDEX index_" + DappDao.TABLE_NAME + "_url ON " + DappDao.TABLE_NAME + " (url)")

            database.execSQL("CREATE TABLE " + BrowserFavoriteDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, url TEXT NOT NULL DEFAULT '', title TEXT, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_" + BrowserFavoriteDao.TABLE_NAME + "_url ON " + BrowserFavoriteDao.TABLE_NAME + " (url)")

            database.execSQL("CREATE TABLE " + BrowserRecentDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL DEFAULT '', url TEXT NOT NULL DEFAULT '', title TEXT, timestamp INTEGER NOT NULL)")
            database.execSQL("CREATE UNIQUE INDEX index_" + BrowserRecentDao.TABLE_NAME + "_host ON " + BrowserRecentDao.TABLE_NAME + " (host)")
        }
    }

    private val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE " + DappRadarDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, radar_id TEXT NOT NULL DEFAULT '', name TEXT NOT NULL DEFAULT '', url TEXT NOT NULL DEFAULT '', icon TEXT, category TEXT NOT NULL DEFAULT '')")
            database.execSQL("CREATE UNIQUE INDEX index_" + DappRadarDao.TABLE_NAME + "_radar_id ON " + DappRadarDao.TABLE_NAME + " (radar_id)")

            database.execSQL("DROP TABLE IF EXISTS " + TransactionsDao.TABLE_NAME)
            database.execSQL("CREATE TABLE " + TransactionsDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, account_id INTEGER NOT NULL, from_recipient_id INTEGER NOT NULL, to_recipient_id INTEGER NOT NULL, token_description_id INTEGER NOT NULL, amount DOUBLE NOT NULL, status TEXT NOT NULL, timestamp INTEGER NOT NULL, tx_hash TEXT NOT NULL, nonce TEXT NOT NULL DEFAULT '')")
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_" + TransactionsDao.TABLE_NAME + "_account_id_from_recipient_id_nonce ON " + TransactionsDao.TABLE_NAME + " (account_id, from_recipient_id, nonce)")
        }
    }

    private val MIGRATION_12_13 = object : Migration(12, 13) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + StakedHistoryDao.TABLE_NAME + " ADD COLUMN eth_two_exited DOUBLE NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE " + StakedHistoryDao.TABLE_NAME + " ADD COLUMN eth_two_addresses_exited TEXT")
        }
    }

    private val MIGRATION_13_14 = object : Migration(13, 14) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE " + CurrencyDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, fiat TEXT NOT NULL DEFAULT '', exchange_rate DOUBLE NOT NULL DEFAULT 0)")
            database.execSQL("CREATE UNIQUE INDEX index_" + CurrencyDao.TABLE_NAME + "_fiat ON " + CurrencyDao.TABLE_NAME + " (fiat)")
        }
    }

    private val MIGRATION_14_15 = object : Migration(14, 15) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS " + BinanceHistoryDao.TABLE_NAME)
            database.execSQL(
                "CREATE TABLE " + BinanceHistoryDao.TABLE_NAME + " (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "address TEXT NOT NULL DEFAULT '', " +
                        "id_key TEXT NOT NULL DEFAULT '', " +
                        "status TEXT NOT NULL DEFAULT '', " +
                        "symbol TEXT NOT NULL DEFAULT '', " +
                        "icon TEXT, " +
                        "amount DOUBLE NOT NULL, " +
                        "from_network TEXT NOT NULL DEFAULT '', " +
                        "to_network TEXT NOT NULL DEFAULT '', " +
                        "from_address TEXT NOT NULL DEFAULT '', " +
                        "to_address TEXT NOT NULL DEFAULT '', " +
                        "deposit_address TEXT NOT NULL DEFAULT '', " +
                        "eth_contract_address TEXT NOT NULL DEFAULT '', " +
                        "bsc_contract_address TEXT NOT NULL DEFAULT '', " +
                        "eth_contract_decimal INTEGER NOT NULL, " +
                        "bsc_contract_decimal INTEGER NOT NULL, " +
                        "swap_fee DOUBLE NOT NULL, " +
                        "swap_fee_rate DOUBLE NOT NULL, " +
                        "network_fee DOUBLE NOT NULL, " +
                        "deposit_timeout INTEGER NOT NULL, " +
                        "deposit_required_confirms TEXT NOT NULL DEFAULT '', " +
                        "create_time INTEGER NOT NULL, " +
                        "deposit_amount DOUBLE NOT NULL, " +
                        "swap_amount DOUBLE NOT NULL, " +
                        "deposit_received_confirms TEXT NOT NULL DEFAULT '', " +
                        "deposit_hash TEXT NOT NULL DEFAULT '', " +
                        "swap_hash TEXT NOT NULL DEFAULT '', " +
                        "exchange_gas_amount TEXT NOT NULL DEFAULT '')"
            )
            database.execSQL("CREATE UNIQUE INDEX index_" + BinanceHistoryDao.TABLE_NAME + "_id_key ON " + BinanceHistoryDao.TABLE_NAME + " (id_key)")

            dropAndCreateLocalTransactionsDao(database)
        }
    }

    private val MIGRATION_15_16 = object : Migration(15, 16) {
        override fun migrate(database: SupportSQLiteDatabase) {
            dropAndCreateLocalTransactionsDao(database)
        }
    }

    private val MIGRATION_16_17 = object : Migration(16, 17) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE " + ExchangeDao.TABLE_NAME + " ADD COLUMN blockchain TEXT NOT NULL DEFAULT 'ETHEREUM'")

            database.execSQL("ALTER TABLE " + TransactionsDao.TABLE_NAME + " ADD COLUMN blockchain TEXT NOT NULL DEFAULT 'ETHEREUM'")
            database.execSQL("DROP INDEX index_" + TransactionsDao.TABLE_NAME + "_account_id_from_recipient_id_nonce")
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_" + TransactionsDao.TABLE_NAME + "_account_id_from_recipient_id_nonce_blockchain ON " + TransactionsDao.TABLE_NAME + " (account_id, from_recipient_id, nonce, blockchain)")

            database.execSQL("ALTER TABLE " + TokenDescriptionDao.TABLE_NAME + " ADD COLUMN blockchain TEXT NOT NULL DEFAULT 'ETHEREUM'")
            database.execSQL("DROP INDEX index_" + TokenDescriptionDao.TABLE_NAME + "_address")
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_" + TokenDescriptionDao.TABLE_NAME + "_address_blockchain ON " + TokenDescriptionDao.TABLE_NAME + " (address,blockchain)")

            database.execSQL("DROP TABLE IF EXISTS " + BinanceHistoryDao.TABLE_NAME)
            database.execSQL(
                "CREATE TABLE " + BinanceHistoryDao.TABLE_NAME + " (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "address TEXT NOT NULL DEFAULT '', " +
                        "id_key TEXT NOT NULL DEFAULT '', " +
                        "status TEXT NOT NULL DEFAULT '', " +
                        "symbol TEXT NOT NULL DEFAULT '', " +
                        "icon TEXT, " +
                        "amount DOUBLE NOT NULL, " +
                        "from_network TEXT NOT NULL DEFAULT '', " +
                        "to_network TEXT NOT NULL DEFAULT '', " +
                        "from_address TEXT NOT NULL DEFAULT '', " +
                        "to_address TEXT NOT NULL DEFAULT '', " +
                        "deposit_address TEXT NOT NULL DEFAULT '', " +
                        "eth_contract_address TEXT NOT NULL DEFAULT '', " +
                        "bsc_contract_address TEXT NOT NULL DEFAULT '', " +
                        "eth_contract_decimal INTEGER NOT NULL, " +
                        "bsc_contract_decimal INTEGER NOT NULL, " +
                        "swap_fee DOUBLE NOT NULL, " +
                        "swap_fee_rate DOUBLE NOT NULL, " +
                        "network_fee DOUBLE NOT NULL, " +
                        "deposit_timeout INTEGER NOT NULL, " +
                        "deposit_required_confirms TEXT NOT NULL DEFAULT '', " +
                        "create_time INTEGER NOT NULL, " +
                        "deposit_amount DOUBLE NOT NULL, " +
                        "swap_amount DOUBLE NOT NULL, " +
                        "deposit_received_confirms TEXT NOT NULL DEFAULT '', " +
                        "deposit_hash TEXT NOT NULL DEFAULT '', " +
                        "swap_hash TEXT NOT NULL DEFAULT ''," +
                        "exchange_gas_amount DOUBLE NOT NULL," +
                        "token_per_bnb DOUBLE NOT NULL)"
            )
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_" + BinanceHistoryDao.TABLE_NAME + "_id_key ON " + BinanceHistoryDao.TABLE_NAME + " (id_key)")

            // Add default token for new blockchains
            with(database.query("SELECT * FROM " + AccountsDao.TABLE_NAME)) {
                if (count > 0) {
                    Blockchain.values().filter { it != Blockchain.ETHEREUM }.forEach { blockchain ->
                        moveToFirst()
                        val descriptionValues = ContentValues().apply {
                            put("address", "")
                            put("decimals", 18)
                            put("name", blockchain.token)
                            put("symbol", blockchain.token)
                            put("logo", "")
                            put("blockchain", blockchain.name)
                        }
                        val descriptionId = database.insert(TokenDescriptionDao.TABLE_NAME, SQLiteDatabase.CONFLICT_IGNORE, descriptionValues)
                        while (!isAfterLast) {
                            val accountId = getLong(getColumnIndex("id"))
                            val assetValues = ContentValues().apply {
                                put("accountId", accountId)
                                put("tokenDescriptionId", descriptionId)
                                put("isPrimary", 1)
                                put("isHidden", 0)
                            }
                            val assetId = database.insert(TokensDao.TABLE_NAME, SQLiteDatabase.CONFLICT_IGNORE, assetValues)
                            val balanceValues = ContentValues().apply {
                                put("tokenId", assetId)
                                put("amount", 0)
                                put("timestamp", 1)
                            }
                            database.insert(BalancesDao.TABLE_NAME, SQLiteDatabase.CONFLICT_IGNORE, balanceValues)
                            moveToNext()
                        }
                    }
                }
            }

        }
    }

    private val MIGRATION_17_18 = object : Migration(17, 18) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS dex_tokens")
            database.execSQL(
                "CREATE TABLE " + SwapTokensDao.TABLE_NAME + " (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "blockchain TEXT NOT NULL DEFAULT 'ETHEREUM'," +
                        "contract_address TEXT NOT NULL DEFAULT ''," +
                        "name TEXT NOT NULL DEFAULT ''," +
                        "symbol TEXT NOT NULL DEFAULT ''," +
                        "icon TEXT NOT NULL DEFAULT ''," +
                        "decimals INTEGER NOT NULL DEFAULT 18," +
                        "timestamp INTEGER NOT NULL DEFAULT 0," +
                        "price DOUBLE NOT NULL DEFAULT 0," +
                        "volume_24h DOUBLE NOT NULL DEFAULT 0)"
            )
            database.execSQL("CREATE UNIQUE INDEX index_swap_tokens_blockchain_contract_address ON " + SwapTokensDao.TABLE_NAME + " (blockchain,contract_address)")

            database.execSQL("DROP TABLE IF EXISTS " + DexPricesDao.TABLE_NAME)
            database.execSQL(
                "CREATE TABLE " + DexPricesDao.TABLE_NAME + " (" +
                        "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "blockchain TEXT NOT NULL," +
                        "exchange TEXT NOT NULL, " +
                        "dex TEXT NOT NULL, " +
                        "price DOUBLE NOT NULL, " +
                        "base TEXT NOT NULL, " +
                        "quote TEXT NOT NULL, " +
                        "scale INTEGER NOT NULL, " +
                        "timestamp INTEGER NOT NULL, " +
                        "marketImpact DOUBLE NOT NULL)"
            )
            database.execSQL("CREATE UNIQUE INDEX index_dex_prices_blockchain_exchange_dex_base_quote_scale ON " + DexPricesDao.TABLE_NAME + " (blockchain,exchange,dex,base,quote,scale)")
        }
    }

    private fun dropAndCreateLocalTransactionsDao(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS " + LocalTransactionsDao.TABLE_NAME)
        database.execSQL("CREATE TABLE " + LocalTransactionsDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, hash TEXT NOT NULL DEFAULT '', nonce TEXT NOT NULL DEFAULT '', from_address TEXT NOT NULL DEFAULT '', to_address TEXT NOT NULL DEFAULT '', value TEXT NOT NULL DEFAULT '', input TEXT NOT NULL DEFAULT '', gas TEXT NOT NULL DEFAULT '', gas_price TEXT NOT NULL DEFAULT '')")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_" + LocalTransactionsDao.TABLE_NAME + "_hash ON " + LocalTransactionsDao.TABLE_NAME + " (hash)")
    }

    private fun dropAndCreateTransactionsDao(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS " + TransactionsDao.TABLE_NAME)
        database.execSQL("CREATE TABLE " + TransactionsDao.TABLE_NAME + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, accountId INTEGER NOT NULL, fromRecipientId INTEGER NOT NULL, toRecipientId INTEGER NOT NULL, tokenDescriptionId INTEGER NOT NULL, amount DOUBLE NOT NULL, status TEXT NOT NULL, timestamp INTEGER NOT NULL, txHash TEXT NOT NULL)")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_transactions_txHash_accountId_tokenDescriptionId_fromRecipientId_toRecipientId ON " + TransactionsDao.TABLE_NAME + " (txHash, accountId, tokenDescriptionId, fromRecipientId, toRecipientId)")
    }

    internal fun init(context: Context) {
        instance = Room
            .databaseBuilder(context, MewDatabase::class.java, MewDatabase.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .addMigrations(MIGRATION_3_4)
            .addMigrations(MIGRATION_4_5)
            .addMigrations(MIGRATION_5_6)
            .addMigrations(MIGRATION_6_7)
            .addMigrations(MIGRATION_7_8)
            .addMigrations(MIGRATION_8_9)
            .addMigrations(MIGRATION_9_10)
            .addMigrations(MIGRATION_10_11)
            .addMigrations(MIGRATION_11_12)
            .addMigrations(MIGRATION_12_13)
            .addMigrations(MIGRATION_13_14)
            .addMigrations(MIGRATION_14_15)
            .addMigrations(MIGRATION_15_16)
            .addMigrations(MIGRATION_16_17)
            .addMigrations(MIGRATION_17_18)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    fun drop(context: Context) {
        isDatabaseRemovingInProgress = true
        context.deleteDatabase(MewDatabase.DB_NAME)
        init(context)
        isDatabaseRemovingInProgress = false
    }
}
