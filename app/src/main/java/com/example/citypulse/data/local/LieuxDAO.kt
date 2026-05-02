package com.example.citypulse.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface LieuxDAO {
    //  fonction pour insérer un lieu
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLieu(lieu: com.example.citypulse.model.Lieux)

    //fonction pour supprimer un lieu
    @Delete
    suspend fun deleteLieu(lieu: com.example.citypulse.model.Lieux)

    // pour la mise à jour
    @Update
    suspend fun updateLieu(lieu: com.example.citypulse.model.Lieux)

    //afficher tous les lieux
    @Query("SELECT * FROM lieux")
    suspend fun getAllLieux(): List<com.example.citypulse.model.Lieux>
}