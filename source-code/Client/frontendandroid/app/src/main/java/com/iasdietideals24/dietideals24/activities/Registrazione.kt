package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase1
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase1Directions
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase2
import com.iasdietideals24.dietideals24.controller.ControllerCreazioneProfiloFase2Directions
import com.iasdietideals24.dietideals24.databinding.ActivityRegistrazioneBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

class Registrazione : DietiDeals24Activity<ActivityRegistrazioneBinding>(), OnChangeActivity,
    OnBackButton, OnNextStep {

    private val viewModel by viewModel<ModelRegistrazione>()

    // Repositories
    private val authRepository: AuthRepository by inject()

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

        if (intent.extras?.getBoolean("skip") != true &&
            intent.data?.scheme == "ias" &&
            intent.data?.host == "com.iasdietideals24.dietideals24" &&
            intent.data?.path == "/signup"
        ) {

            val code = intent.data?.getQueryParameter("code") ?: ""

            lifecycleScope.launch {
                CurrentUser.jwt = withContext(Dispatchers.IO) {
                    authRepository.recuperaJWT(
                        code,
                        "ias://com.iasdietideals24.dietideals24/signup"
                    )
                }

                withContext(Dispatchers.IO) {
                    authRepository.scriviJWT(CurrentUser.jwt)
                }

                if (CurrentUser.jwt != "") {
                    JWT.getUserEmail(CurrentUser.jwt)

                    Intent(
                        baseContext,
                        ScelteIniziali::class.java
                    )

                    finish()
                }
            }
        } else {
            viewModel.email.value = intent.extras?.getString("email") ?: ""
            viewModel.password.value = intent.extras?.getString("email") ?: ""
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_registrazione_fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    override fun <Activity : AppCompatActivity> onChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
        finishAffinity()
    }

    override fun onBackButton() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_registrazione_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        if (navHostFragment.childFragmentManager.backStackEntryCount > 1)
            navController.popBackStack()
        else
            finish()
    }

    override fun onNextStep(sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ControllerCreazioneProfiloFase1::class -> {
                val action =
                    ControllerCreazioneProfiloFase1Directions.actionControllerCreazioneProfiloFase1ToControllerCreazioneProfiloFase2()
                navController.navigate(action)
            }

            ControllerCreazioneProfiloFase2::class -> {
                val action =
                    ControllerCreazioneProfiloFase2Directions.actionControllerCreazioneProfiloFase2ToControllerCreazioneProfiloFase3()
                navController.navigate(action)
            }
        }
    }
}