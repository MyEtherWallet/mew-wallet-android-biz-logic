package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.BrowserRecentDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import java.util.*

@Entity(tableName = BrowserRecentDao.TABLE_NAME, indices = [Index(value = ["host","blockchain"], unique = true)])
data class EntityBrowserRecent(
    val host: String,
    val url: String,
    val title: String?,
    val timestamp: Date,
    @ColumnInfo(name = "blockchain", defaultValue = "ETHEREUM")
    val blockchain: Blockchain
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}