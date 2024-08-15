package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeDate

class Registrazione : AppCompatActivity(R.layout.activity_registrazione), OnFragmentChangeActivity,
    OnFragmentBackButton, OnFragmentChangeDate {

    private lateinit var viewModel: ModelRegistrazione

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrazione)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(ModelRegistrazione::class)
    }

    override fun onStop() {
        super.onStop()

        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            LoginManager.getInstance().logOut()
        }
    }

    override fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
    }

    override fun onFragmentBackButton() {
        if (supportFragmentManager.fragments[0].childFragmentManager.backStackEntryCount > 0)
            findNavController(R.id.registrazione_fragmentContainerView).popBackStack()
        else
            finishAndRemoveTask()
    }

    override fun onFragmentChangeDate(string: String) {
        supportFragmentManager.fragments[0].let {
            findViewById<TextInputEditText>(R.id.creazioneProfiloFase1_dataNascita)?.setText(string)
        }
    }
}