package uk.co.applylogic.marvel.data.room.dao.model

import androidx.annotation.Nullable
import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity(tableName = "searchResults")
data class ResultsModel(
        @JvmField
        @PrimaryKey
        @NotNull
        @ColumnInfo(name = "id") var id: String,
        @JvmField
        @Nullable
        @ColumnInfo(name = "timetag") var timetag: Long? = null,
        @JvmField
        @Nullable
        @ColumnInfo(name = "data") var data: String? = null)
