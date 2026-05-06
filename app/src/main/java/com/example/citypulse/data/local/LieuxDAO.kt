package com.example.citypulse.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.citypulse.model.Lieux

@Dao
interface LieuxDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLieu(lieu: Lieux)

    @Delete
    suspend fun deleteLieu(lieu: Lieux)

    @Update
    suspend fun updateLieu(lieu: Lieux)

    @Query("SELECT * FROM lieux WHERE idlieu = :lieuId")
    suspend fun getLieuById(lieuId: Int): Lieux?

    @Query("UPDATE lieux SET estFavori = :estFavori WHERE idlieu = :lieuId")
    suspend fun updateFavoriStatus(lieuId: Int, estFavori: Int)

    @Query("UPDATE lieux SET notePersonnelle = :note WHERE idlieu = :lieuId")
    suspend fun updateNotePersonnelle(lieuId: Int, note: String)

    @Query("SELECT * FROM lieux")
    fun getAllLieux(): LiveData<List<Lieux>>

    @Query("SELECT * FROM lieux")
    suspend fun getAllLieuxList(): List<Lieux>

    @Query("SELECT * FROM lieux WHERE estFavori = 1")
    fun getFavoris(): LiveData<List<Lieux>>

    @Query("SELECT * FROM lieux WHERE estFavori = 1")
    suspend fun getFavorisList(): List<Lieux>
}