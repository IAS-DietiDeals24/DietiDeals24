package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentHideBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentShowBackButton


class ScelteIniziali : AppCompatActivity(R.layout.activity_scelte_iniziali),
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scelteIniziali)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatImageButton>(R.id.activity_scelteIniziali_pulsanteIndietro)?.setOnClickListener {
            if (supportFragmentManager.fragments[0].childFragmentManager.backStackEntryCount > 0)
                findNavController(R.id.activity_scelteIniziali_fragmentContainerView).popBackStack()
            else
                finishAndRemoveTask()
        }
    }

    override fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
    }

    override fun onFragmentHideBackButton() {
        findViewById<AppCompatImageButton>(R.id.activity_scelteIniziali_pulsanteIndietro)?.visibility =
            android.view.View.GONE
    }

    override fun onFragmentShowBackButton() {
        findViewById<AppCompatImageButton>(R.id.activity_scelteIniziali_pulsanteIndietro)?.visibility =
            android.view.View.VISIBLE
    }
}