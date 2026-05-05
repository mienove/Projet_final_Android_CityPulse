package com.example.citypulse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lieux")
data class Lieux(
    @PrimaryKey(autoGenerate = true)
    val idlieu: Int = 0,
    val nomlieu: String,
    val adresse: String,
    val photo: String,
    val categorie: String,
    val latitude: Double,
    val longitude: Double,

    @ColumnInfo(defaultValue = "0")
    val estFavori: Int = 0,

    @ColumnInfo(defaultValue = "''")
    val notePersonnelle: String = "",
)
