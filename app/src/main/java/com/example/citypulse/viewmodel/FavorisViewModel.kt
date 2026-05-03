package com.example.citypulse.viewmodel

import androidx.lifecycle.ViewModel
import com.example.citypulse.data.local.FavorisDAO

class FavorisViewModel(
    private val dao: FavorisDAO
): ViewModel(){
    val favoris = dao.getAllFavoris();
}