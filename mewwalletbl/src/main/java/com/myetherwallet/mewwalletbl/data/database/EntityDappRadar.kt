package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DappRadarDao

@Entity(tableName = DappRadarDao.TABLE_NAME, indices = [Index(value = ["radar_id"], unique = true)])
class EntityDappRadar(
    @ColumnInfo(name = "radar_id")
    val radarId: String,
    val name: String,
    val url: String,
    val icon: String? = null,
    val category: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
