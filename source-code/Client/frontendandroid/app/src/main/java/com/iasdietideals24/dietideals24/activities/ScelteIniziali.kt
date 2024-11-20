package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerSelezioneTipoAccount
import com.iasdietideals24.dietideals24.controller.ControllerSelezioneTipoAccountDirections
import com.iasdietideals24.dietideals24.databinding.ActivityScelteInizialiBinding
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.kscripts.OnHideBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.kscripts.OnShowBackButton
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlin.reflect.KClass


class ScelteIniziali : DietiDeals24Activity<ActivityScelteInizialiBinding>(), OnChangeActivity,
    OnHideBackButton, OnShowBackButton, OnNextStep {

    override fun onCreate(savedInstanceState: Bundle?) {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            CurrentUser.id = "andrylook14@gmail.com"
            CurrentUser.tipoAccount = TipoAccount.COMPRATORE
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

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_scelteIniziali_fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    override fun <Activity : AppCompatActivity> onChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))

        if (activity == Home::class.java) {
            finish()
        }
    }

    override fun onHideBackButton() {
        binding.activityScelteInizialiPulsanteIndietro.visibility =
            android.view.View.GONE
    }

    override fun onShowBackButton() {
        binding.activityScelteInizialiPulsanteIndietro.visibility =
            android.view.View.VISIBLE
    }

    override fun onNextStep(sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ControllerSelezioneTipoAccount::class -> {
                val action =
                    ControllerSelezioneTipoAccountDirections.actionControllerSelezioneTipoAccountToControllerSelezioneAccessoRegistrazione()
                navController.navigate(action)
            }
        }
    }
}