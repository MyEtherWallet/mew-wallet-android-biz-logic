package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.BrowserRecentDao
import java.util.*

@Entity(tableName = BrowserRecentDao.TABLE_NAME, indices = [Index(value = ["host"], unique = true)])
data class EntityBrowserRecent(
    val host: String,
    val url: String,
    val title: String?,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}