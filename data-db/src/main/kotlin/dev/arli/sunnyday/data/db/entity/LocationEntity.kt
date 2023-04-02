package dev.arli.sunnyday.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = LocationEntity.TableName,
    primaryKeys = [
        LocationEntity.Columns.Latitude,
        LocationEntity.Columns.Longitude
    ]
)
data class LocationEntity(
    @ColumnInfo(name = Columns.Latitude) val latitude: Double,
    @ColumnInfo(name = Columns.Longitude) val longitude: Double,
    @ColumnInfo(name = Columns.Name) val name: String?,
    @ColumnInfo(name = Columns.IsCurrent) val isCurrent: Boolean
) {

    companion object {
        const val TableName = "location"
    }

    object Columns {
        const val Latitude = "latitude"
        const val Longitude = "longitude"
        const val Name = "name"
        const val IsCurrent = "is_current"
    }
}
