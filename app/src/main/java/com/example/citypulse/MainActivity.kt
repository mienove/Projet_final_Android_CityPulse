// Déclare le package de ton application
package com.example.citypulse

// Import des bibliothèques nécessaires
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Définition de l'activité principale qui gère la carte
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // Variable qui contiendra la carte Google Maps
    private lateinit var map: GoogleMap

    // Déclaration du launcher pour la permission (doit être fait avant le démarrage de l'activité)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // Fonction appelée au démarrage de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Charge le fichier XML qui définit l'interface graphique
        setContentView(R.layout.activity_main)

        // Initialisation du launcher de permission
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Si l'utilisateur accepte, on récupère sa position
                getUserLocation()
            } else {
                // Sinon, on peut afficher un message à l'utilisateur
            }
        }

        // Récupère le fragment de la carte défini dans le layout XML
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        // Demande à être prévenu quand la carte est prête
        mapFragment.getMapAsync(this)
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
