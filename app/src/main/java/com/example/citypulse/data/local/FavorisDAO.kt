package com.example.citypulse.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.citypulse.model.Favoris

@Dao
interface FavorisDAO {
    //methode pour inserer un favori
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoris(favoris: com.example.citypulse.model.Favoris)

    // methode pour supprimer un favors
    @Delete
    suspend fun deleteFavoris(favoris: com.example.citypulse.model.Favoris)

    // Mettre à jour
    @Update
    suspend fun updateFavoris(favoris: com.example.citypulse.model.Favoris)

    // Obtenir tous les favoris
    @Query("SELECT * FROM favoris")
     fun getAllFavoris(): LiveData<List<Favoris>>

    // Obtenir un favoris par ID
    @Query("SELECT * FROM favoris WHERE idfavoris = :id")
    suspend fun getFavorisById(id: Int): com.example.citypulse.model.Favoris?
}