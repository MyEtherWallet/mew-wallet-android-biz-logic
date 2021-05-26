package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.BrowserFavoriteDao
import java.util.*

@Entity(tableName = BrowserFavoriteDao.TABLE_NAME, indices = [Index(value = ["url"], unique = true)])
data class EntityBrowserFavorite(
    val url: String,
    val title: String?,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}