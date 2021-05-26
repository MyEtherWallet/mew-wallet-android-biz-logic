package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DappDao

@Entity(tableName = DappDao.TABLE_NAME, indices = [Index(value = ["url"], unique = true)])
class EntityDapp (
    val url: String,
    val name: String,
    val desc: String,
    val img: String,
    val category: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}