package com.example.citypulse.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utilisateurs")
data class Utilisateurs(
    @PrimaryKey(autoGenerate = true)
    val idutilisateur: Int = 0,

    val nom_utilisateur: String,
    val prenom_utilisateur: String,
    val emailUtil: String,
    val phone: String
)
