package com.example.citypulse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citypulse.data.local.LieuxDAO
import com.example.citypulse.model.Lieux
import kotlinx.coroutines.launch

class LieuxViewModel (
    private val daolieu: LieuxDAO
): ViewModel(){
    val lieu = MutableLiveData<List<Lieux>>()
//    charger les lieux
    fun loadLieux(){
        viewModelScope.launch{
            lieu.value = daolieu.getAllLieux() as List<Lieux>?
        }
    }
}