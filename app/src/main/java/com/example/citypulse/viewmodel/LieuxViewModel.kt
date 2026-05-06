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
<<<<<<< HEAD
        viewModelScope.launch{
=======
    val viewModelScope = null
    viewModelScope.launch{
>>>>>>> 45af0e7de4f1aabb4829a3d6f52febab008c0acb
            lieu.value = daolieu.getAllLieux() as List<Lieux>?
        }
    }
}