// Déclare le package de ton application
package com.example.citypulse

// Import des bibliothèques nécessaires
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

// Définition de l'activité principale qui gère la carte
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // Variable qui contiendra la carte Google Maps
    private lateinit var map: GoogleMap

    // Déclaration du launcher pour la permission (doit être fait avant le démarrage de l'activité)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // ========== NOUVEAU : Variables pour l'interface ==========
    private lateinit var buttonMap: LinearLayout
    private lateinit var buttonList: LinearLayout
    private lateinit var buttonFavorites: LinearLayout
    private lateinit var bottomNav: BottomNavigationView

    // Variable pour savoir quel écran est affiché
    private var currentScreen = "home" // home, map, list, favorites

    // Fonction appelée au démarrage de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Charge le fichier XML qui définit l'interface graphique
        setContentView(R.layout.activity_main)

        // ========== NOUVEAU : Initialisation des éléments de l'interface ==========
        initUI()

        // Initialisation du launcher de permission
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Si l'utilisateur accepte, on récupère sa position
                getUserLocation()
            } else {
                Toast.makeText(this, "Localisation refusée : affichage limité", Toast.LENGTH_LONG).show()
            }
        }

        // Récupère le fragment de la carte défini dans le layout XML
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        // Demande à être prévenu quand la carte est prête
        mapFragment.getMapAsync(this)
    }

    // ========== NOUVEAU : Initialisation de l'interface ==========
    private fun initUI() {
        // Récupération des boutons de l'écran d'accueil
        buttonMap = findViewById(R.id.buttonMap)
        buttonList = findViewById(R.id.buttonList)
        buttonFavorites = findViewById(R.id.buttonFavorites)

        // Configuration des clics sur les boutons
        buttonMap.setOnClickListener {
            Toast.makeText(this, "Ouverture de la carte", Toast.LENGTH_SHORT).show()
            showMap()
        }

        buttonList.setOnClickListener {
            Toast.makeText(this, "Ouverture de la liste", Toast.LENGTH_SHORT).show()
            // TODO: Afficher la liste
        }

        buttonFavorites.setOnClickListener {
            Toast.makeText(this, "Ouverture des favoris", Toast.LENGTH_SHORT).show()
            // TODO: Afficher les favoris
        }

        // Récupération de la barre de navigation
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_map -> {
                    Toast.makeText(this, "Carte", Toast.LENGTH_SHORT).show()
                    showMap()
                    true
                }
                R.id.navigation_list -> {
                    Toast.makeText(this, "Liste", Toast.LENGTH_SHORT).show()
                    // TODO: Afficher la liste
                    true
                }
                R.id.navigation_favorites -> {
                    Toast.makeText(this, "Favoris", Toast.LENGTH_SHORT).show()
                    // TODO: Afficher les favoris
                    true
                }
                else -> false
            }
        }
    }

    // ========== NOUVEAU : Afficher la carte ==========
    private fun showMap() {
        currentScreen = "map"
        // Rendre la carte visible
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.view?.visibility = android.view.View.VISIBLE

        // Cacher l'écran d'accueil
        val scrollView = findViewById<android.widget.ScrollView>(R.id.scrollViewHome)
        scrollView?.visibility = android.view.View.GONE
    }

    // ========== NOUVEAU : Afficher l'écran d'accueil ==========
    private fun showHome() {
        currentScreen = "home"
        // Cacher la carte
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.view?.visibility = android.view.View.GONE

        // Afficher l'écran d'accueil
        val scrollView = findViewById<android.widget.ScrollView>(R.id.scrollViewHome)
        scrollView?.visibility = android.view.View.VISIBLE
    }

    // Fonction appelée automatiquement quand la carte est prête
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap // On stocke la carte dans la variable
        requestLocationPermission() // On demande la permission de localisation
    }

    // Fonction pour demander la permission d'accéder à la localisation
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission déjà accordée
            getUserLocation()
        } else {
            // Lance la demande de permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Fonction pour récupérer la position de l'utilisateur
    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        // Vérifie que la permission est bien accordée (double sécurité pour le lint)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Service qui fournit la localisation
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            // Récupère la dernière position connue
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    // Crée un objet LatLng avec latitude et longitude
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    // Ajoute un marqueur sur la carte
                    map.addMarker(MarkerOptions().position(userLatLng).title("Vous êtes ici"))
                    // Déplace la caméra pour centrer sur la position de l’utilisateur
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
                }
            }
        }
    }
}