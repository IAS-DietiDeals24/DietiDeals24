package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase1
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase1Directions
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase2
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase2Directions
import com.iasdietideals24.dietideals24.controller.ControllerRegistrazione
import com.iasdietideals24.dietideals24.controller.ControllerRegistrazioneDirections
import com.iasdietideals24.dietideals24.controller.ControllerSelezioneTipoAccount
import com.iasdietideals24.dietideals24.controller.ControllerSelezioneTipoAccountDirections
import com.iasdietideals24.dietideals24.databinding.ActivityRegistrazioneBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentNextStep
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentSkipStep
import kotlin.reflect.KClass

class Registrazione : DietiDeals24Activity<ActivityRegistrazioneBinding>(),
    OnFragmentChangeActivity, OnFragmentBackButton, OnFragmentNextStep, OnFragmentSkipStep {

    private lateinit var viewModel: ModelRegistrazione

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.registrazione) { v, insets ->
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
        finishAffinity()
    }

    override fun onFragmentBackButton() {
        findNavController(R.id.activity_registrazione_fragmentContainerView).popBackStack()
    }

    override fun onFragmentNextStep(sender: KClass<*>) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_registrazione_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            sender == ControllerSelezioneTipoAccount::class -> {
                val action =
                    ControllerSelezioneTipoAccountDirections.actionControllerSelezioneTipoAccountToControllerSelezioneAccessoRegistrazione()
                navController.navigate(action)
            }

            sender == ControllerRegistrazione::class -> {
                val action =
                    ControllerRegistrazioneDirections.actionControllerRegistrazioneToControllerCreazioneProfiloFase1()
                navController.navigate(action)
            }

            sender == ControllerCreazioneProfiloFase1::class -> {
                val action =
                    ControllerCreazioneProfiloFase1Directions.actionControllerCreazioneProfiloFase1ToControllerCreazioneProfiloFase2()
                navController.navigate(action)
            }

            sender == ControllerCreazioneProfiloFase2::class -> {
                val action =
                    ControllerCreazioneProfiloFase2Directions.actionControllerCreazioneProfiloFase2ToControllerCreazioneProfiloFase3()
                navController.navigate(action)
            }
        }
    }

    override fun onFragmentSkipStep(sender: KClass<*>) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_registrazione_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            sender == ControllerRegistrazione::class -> {
                val action =
                    ControllerRegistrazioneDirections.actionControllerRegistrazioneToControllerAssociazioneProfilo()
                navController.navigate(action)
            }
        }
    }
}