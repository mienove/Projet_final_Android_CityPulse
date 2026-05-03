package com.example.citypulse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.citypulse.data.local.LieuxDAO
import com.example.citypulse.model.Lieux

class LieuxViewModel (
    private val daolieu: LieuxDAO
): ViewModel(){
    val lieu = MutableLiveData<List<Lieux>>()
//    charger les lieux
    fun loadLieux(){
        viewModelScope.launch{
            lieu.value = daolieu.getAllLieux()
        }
    }
}