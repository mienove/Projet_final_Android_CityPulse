package com.example.citypulse.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.citypulse.R
import com.example.citypulse.data.local.CityPulseDatabase
import com.example.citypulse.model.Lieux
import com.example.citypulse.viewmodel.PlaceDetailViewModel

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaceDetailViewModel

    private lateinit var textViewNomLieu: TextView
    private lateinit var textViewCategory: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewCoordinates: TextView
    private lateinit var buttonFavorite: ImageButton
    private lateinit var editTextNote: EditText
    private lateinit var buttonSaveNote: Button
    private lateinit var buttonBack: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        initViews()

        val lieuId = intent.getIntExtra("lieu_id", -1)
        if (lieuId == -1) {
            Toast.makeText(this, "Erreur: ID du lieu non trouvé", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = CityPulseDatabase.getInstance(this)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PlaceDetailViewModel(database.lieuxDAO()) as T
            }
        })[PlaceDetailViewModel::class.java]

        viewModel.lieu.observe(this) { lieu ->
            lieu?.let { afficherDetails(it) }
        }

        viewModel.message.observe(this) { msg ->
            msg?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        buttonFavorite.setOnClickListener {
            viewModel.toggleFavori()
        }

        buttonSaveNote.setOnClickListener {
            val note = editTextNote.text.toString()
            if (note.isNotEmpty()) {
                viewModel.sauvegarderNote(note)
                Toast.makeText(this, "Note sauvegardée", Toast.LENGTH_SHORT).show()
                editTextNote.clearFocus()
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }

        viewModel.chargerLieu(lieuId)
    }

    private fun initViews() {
        textViewNomLieu = findViewById(R.id.textViewNomLieu)
        textViewCategory = findViewById(R.id.textViewCategory)
        textViewAddress = findViewById(R.id.textViewAddress)
        textViewCoordinates = findViewById(R.id.textViewCoordinates)
        buttonFavorite = findViewById(R.id.buttonFavorite)
        editTextNote = findViewById(R.id.editTextNote)
        buttonSaveNote = findViewById(R.id.buttonSaveNote)
        buttonBack = findViewById(R.id.buttonBack)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun afficherDetails(lieu: Lieux) {
        textViewNomLieu.text = lieu.nomlieu
        textViewCategory.text = lieu.categorie
        textViewAddress.text = lieu.adresse ?: "Adresse non disponible"
        textViewCoordinates.text = String.format("%.6f, %.6f", lieu.latitude, lieu.longitude)

        // Utilisation des icônes Android par défaut
        val icon = if (viewModel.estFavori())
            android.R.drawable.btn_star_big_on
        else
            android.R.drawable.btn_star_big_off
        buttonFavorite.setImageResource(icon)

        editTextNote.setText(viewModel.getNote())
    }
}