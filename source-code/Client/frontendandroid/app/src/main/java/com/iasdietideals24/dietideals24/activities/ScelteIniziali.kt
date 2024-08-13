package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity

class ScelteIniziali : AppCompatActivity(R.layout.activity_scelte_iniziali),
    OnFragmentChangeActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scelteIniziali)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<AppCompatImageButton>(R.id.scelteIniziali_pulsanteIndietro)?.setOnClickListener {
            if (supportFragmentManager.fragments[0].childFragmentManager.backStackEntryCount > 0)
                findNavController(R.id.scelteIniziali_fragmentContainerView).popBackStack()
            else
                finishAndRemoveTask()
        }
    }

    override fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
    }
}