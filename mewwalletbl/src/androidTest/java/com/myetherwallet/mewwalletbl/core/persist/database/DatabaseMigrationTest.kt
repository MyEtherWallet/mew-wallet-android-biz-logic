package com.myetherwallet.mewwalletbl.core.persist.database

import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.myetherwallet.mewwalletbl.core.MewLog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.reflect.full.memberProperties

/**
 * Created by BArtWell on 25.11.2021.
 */

private const val TAG = "DatabaseMigrationTest"

@RunWith(AndroidJUnit4ClassRunner::class)
class DatabaseMigrationTest {

    private val migrations = mutableListOf<Migration>()

    init {
        val kClass = Database::class
        val instance = kClass.objectInstance ?: kClass.java.newInstance()
        kClass.memberProperties.forEach { field ->
            if (field.name.startsWith("MIGRATION_")) {
                MewLog.d(TAG, "Found " + field.name)
                migrations.add(field.get(instance) as Migration)
            }
        }
        for (autoMigration in MewDatabase_Impl().getAutoMigrations(emptyMap())) {
            migrations.add(autoMigration)
        }
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MewDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun testAllMigrations() {
        helper
            .createDatabase(MewDatabase.DB_NAME, 1)
            .apply {
                close()
            }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MewDatabase::class.java,
            MewDatabase.DB_NAME
        ).addMigrations(*migrations.toTypedArray())
            .build()
            .apply {
                openHelper.writableDatabase
                close()
            }
    }
}
