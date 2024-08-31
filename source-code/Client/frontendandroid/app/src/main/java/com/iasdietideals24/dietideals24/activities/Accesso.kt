package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.iasdietideals24.dietideals24.databinding.ActivityAccessoBinding
import com.iasdietideals24.dietideals24.model.ModelAccesso
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity

class Accesso : DietiDeals24Activity<ActivityAccessoBinding>(), OnChangeActivity, OnBackButton {

    private lateinit var viewModel: ModelAccesso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.accesso) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ModelAccesso::class]
    }

    override fun <Activity : AppCompatActivity> onChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
        finishAffinity()
    }

    override fun onBackButton() {
        finish()
    }
}