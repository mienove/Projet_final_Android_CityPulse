package com.example.citypulse.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDAO {

    //  Ajouter un utilisateur
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtilisateur(utilisateur: com.example.citypulse.model.Utilisateurs)

    //  Supprimer un utilisateur
    @Delete
    suspend fun deleteUtilisateur(utilisateur: com.example.citypulse.model.Utilisateurs)

    // Mettre à jour un utilisateur
    @Update
    suspend fun updateUtilisateur(utilisateur: com.example.citypulse.model.Utilisateurs)

    // Obtenir tous les utilisateurs
    @Query("SELECT * FROM utilisateurs")
    suspend fun getAllUtilisateurs(): List<com.example.citypulse.model.Utilisateurs>

    // Rechercher par ID
    @Query("SELECT * FROM utilisateurs WHERE idutilisateur = :id")
    suspend fun getUtilisateurById(id: Int): com.example.citypulse.model.Utilisateurs?

    //  Rechercher par email
    @Query("SELECT * FROM utilisateurs WHERE emailUtil = :email")
    suspend fun getUtilisateurByEmail(email: String): com.example.citypulse.model.Utilisateurs?


}