package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.ActivityScelteInizialiBinding
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentHideBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentShowBackButton


class ScelteIniziali : DietiDeals24Activity<ActivityScelteInizialiBinding>(),
    OnFragmentChangeActivity, OnFragmentHideBackButton, OnFragmentShowBackButton {

    override fun onCreate(savedInstanceState: Bundle?) {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            startActivity(Intent(baseContext, Home::class.java))
        } else {
            LoginManager.getInstance().logOut()
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.scelteIniziali) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.activityScelteInizialiPulsanteIndietro.setOnClickListener {
            findNavController(R.id.activity_scelteIniziali_fragmentContainerView).popBackStack()
        }
    }

    override fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, Home::class.java))
        finishAffinity()
    }

    override fun onFragmentHideBackButton() {
        binding.activityScelteInizialiPulsanteIndietro.visibility =
            android.view.View.GONE
    }

    override fun onFragmentShowBackButton() {
        binding.activityScelteInizialiPulsanteIndietro.visibility =
            android.view.View.VISIBLE
    }
}