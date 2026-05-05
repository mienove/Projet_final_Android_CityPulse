package com.example.citypulse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citypulse.data.local.LieuxDAO
import com.example.citypulse.model.Lieux
import kotlinx.coroutines.launch

class PlaceDetailViewModel(
    private val lieuxDAO: LieuxDAO
) : ViewModel() {

    private val _lieu = MutableLiveData<Lieux?>()
    val lieu: LiveData<Lieux?> = _lieu

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun chargerLieu(lieuId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val lieu = lieuxDAO.getLieuById(lieuId)
                _lieu.value = lieu
            } catch (e: Exception) {
                _message.value = "Erreur: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavori() {
        viewModelScope.launch {
            val lieuActuel = _lieu.value ?: return@launch
            val nouveauStatut = if (lieuActuel.estFavori == 1) 0 else 1

            lieuxDAO.updateFavoriStatus(lieuActuel.idlieu, nouveauStatut)

            _lieu.value = lieuActuel.copy(estFavori = nouveauStatut)

            val message = if (nouveauStatut == 1) "Ajouté aux favoris" else "Retiré des favoris"
            _message.value = message
        }
    }

    fun sauvegarderNote(note: String) {
        viewModelScope.launch {
            val lieuActuel = _lieu.value ?: return@launch

            lieuxDAO.updateNotePersonnelle(lieuActuel.idlieu, note)

            _lieu.value = lieuActuel.copy(notePersonnelle = note)
            _message.value = "Note sauvegardée"
        }
    }

    fun estFavori(): Boolean = _lieu.value?.estFavori == 1

    fun getNote(): String = _lieu.value?.notePersonnelle ?: ""
}