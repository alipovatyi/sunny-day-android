package dev.arli.sunnyday.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocationEntity.TableName)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "is_current") val isCurrent: Boolean
) {

    companion object {
        const val TableName = "location"
    }
}
