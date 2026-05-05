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
    // Fonction pour insérer un lieu
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLieu(lieu: Lieux)

    // Fonction pour supprimer un lieu
    @Delete
    suspend fun deleteLieu(lieu: Lieux)

    // Pour la mise à jour
    @Update
    suspend fun updateLieu(lieu: Lieux)

    // Pour la fiche détail - récupérer 1 seul lieu
    // ✅ CORRIGÉ: idLieu est Int (correspond au modèle)
    @Query("SELECT * FROM lieux WHERE idlieu = :lieuId")
    suspend fun getLieuById(lieuId: Int): Lieux?

    // Pour modifier le statut favori
    // ✅ CORRIGÉ: lieuId est Int (pas String)
    @Query("UPDATE lieux SET estFavori = :estFavori WHERE idlieu = :lieuId")
    suspend fun updateFavoriStatus(lieuId: Int, estFavori: Int)

    // Pour modifier la note personnelle
    // ✅ CORRIGÉ: lieuId est Int (pas String)
    @Query("UPDATE lieux SET notePersonnelle = :note WHERE idlieu = :lieuId")
    suspend fun updateNotePersonnelle(lieuId: Int, note: String)

    // Afficher tous les lieux
    @Query("SELECT * FROM lieux")
    fun getAllLieux(): LiveData<List<Lieux>>
}