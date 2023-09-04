package su.pank.workwithroom.enities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(@PrimaryKey val id: Int, @ColumnInfo("last_name") val last_name: String,  @ColumnInfo("first_name") val first_name: String)