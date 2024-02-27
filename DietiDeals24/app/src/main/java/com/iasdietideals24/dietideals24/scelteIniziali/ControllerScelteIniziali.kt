package com.iasdietideals24.dietideals24.scelteIniziali

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.iasdietideals24.dietideals24.R

class ControllerScelteIniziali : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FrameScelteIniziali(this)
    }
}

/*
Template: da inserire al momento di creazione della activity, indica il comportamento da adottare
dopo che l'utente ha effettuato la sua scelta nel popup di richiesta dei permessi

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Cosa fare se l'utente preme "consenti"
                } else {
                    // Cosa fare se l'utente preme "non consentire"
                }
            }

Template: da inserire ogni qual volta si effettui un'azione che richieda un permesso

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            // Eseguire qui l'azione che richiedeva il permesso
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Si può inserire un popup informativo dove si spiega perché si sta richiedendo il permesso
        } else {
            // Mostra il popup di sistema per richiedere il permesso
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE);
        }
 */