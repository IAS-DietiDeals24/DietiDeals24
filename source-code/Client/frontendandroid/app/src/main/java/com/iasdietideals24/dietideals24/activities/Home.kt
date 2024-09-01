package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerAsteCreateDirections
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAsta
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAstaDirections
import com.iasdietideals24.dietideals24.controller.ControllerDettagliProfilo
import com.iasdietideals24.dietideals24.controller.ControllerDettagliProfiloDirections
import com.iasdietideals24.dietideals24.controller.ControllerHomeDirections
import com.iasdietideals24.dietideals24.controller.ControllerModificaAsta
import com.iasdietideals24.dietideals24.controller.ControllerModificaAstaDirections
import com.iasdietideals24.dietideals24.controller.ControllerModificaProfilo
import com.iasdietideals24.dietideals24.controller.ControllerModificaProfiloDirections
import com.iasdietideals24.dietideals24.controller.ControllerNotificheDirections
import com.iasdietideals24.dietideals24.controller.ControllerOfferteDirections
import com.iasdietideals24.dietideals24.controller.ControllerPartecipazioniDirections
import com.iasdietideals24.dietideals24.controller.ControllerProfilo
import com.iasdietideals24.dietideals24.controller.ControllerProfiloDirections
import com.iasdietideals24.dietideals24.databinding.ActivityHomeBinding
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderAstaCreata
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderNotifica
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderOfferta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderPartecipazione
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToCreatedAuctions
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToHome
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToParticipation
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.interfaces.OnOpenUrl
import com.iasdietideals24.dietideals24.utilities.interfaces.OnRefresh
import kotlin.reflect.KClass

class Home : DietiDeals24Activity<ActivityHomeBinding>(), OnBackButton, OnGoToProfile, OnEditButton,
    OnGoToBids, OnRefresh, OnOpenUrl, OnGoToDetails, OnGoToParticipation, OnChangeActivity,
    OnGoToHome, OnGoToCreatedAuctions {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.home) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.activityHomeBottomNavigationView.setupWithNavController(navController)

        if (CurrentUser.id == 0L) {
            binding.activityHomeBottomNavigationView.menu.getItem(1).isEnabled = false
            binding.activityHomeBottomNavigationView.menu.getItem(2).isEnabled = false
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onBackButton() {
        val navController = getNavController()

        navController.popBackStack()
    }

    override fun onGoToDetails(id: Long, sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ViewHolderAnteprimaAsta::class -> {
                val action =
                    ControllerHomeDirections.actionControllerHomeToControllerDettagliAsta(id)
                navController.navigate(action)
            }

            ViewHolderNotifica::class -> {
                val action =
                    ControllerNotificheDirections.actionControllerNotificheToControllerDettagliAsta(
                        id
                    )
                navController.navigate(action)
            }

            ControllerModificaAsta::class -> {
                val action =
                    ControllerModificaAstaDirections.actionControllerModificaAstaToControllerDettagliAsta(
                        id
                    )
                navController.navigate(action)
            }

            ViewHolderPartecipazione::class -> {
                val action =
                    ControllerPartecipazioniDirections.actionControllerPartecipazioniToControllerDettagliAsta(
                        id
                    )
                navController.navigate(action)
            }

            ViewHolderAstaCreata::class -> {
                val action =
                    ControllerAsteCreateDirections.actionControllerAsteCreateToControllerDettagliAsta(
                        id
                    )
                navController.navigate(action)
            }
        }
    }

    override fun onGoToProfile(id: Long, sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ViewHolderNotifica::class -> {
                val action =
                    ControllerNotificheDirections.actionControllerNotificheToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            ControllerDettagliAsta::class -> {
                val action =
                    ControllerDettagliAstaDirections.actionControllerDettagliAstaToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            ControllerProfilo::class -> {
                val action =
                    ControllerProfiloDirections.actionControllerProfiloToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            ControllerModificaProfilo::class -> {
                val action =
                    ControllerModificaProfiloDirections.actionControllerModificaProfiloToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }
        }
    }

    override fun onEditButton(id: Long, sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ControllerDettagliProfilo::class -> {
                val action =
                    ControllerDettagliProfiloDirections.actionControllerDettagliProfiloToControllerModificaProfilo()
                navController.navigate(action)
            }

            ControllerDettagliAsta::class -> {
                val action =
                    ControllerDettagliAstaDirections.actionControllerDettagliAstaToControllerModificaAsta()
                navController.navigate(action)
            }

            ViewHolderAstaCreata::class -> {
                val action =
                    ControllerAsteCreateDirections.actionControllerAsteCreateToControllerModificaAsta(
                        id
                    )
                navController.navigate(action)
            }
        }
    }

    override fun <Activity : AppCompatActivity> onChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
        finishAffinity()
    }

    override fun onOpenUrl(externalUrl: String) {
        var url = externalUrl

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onGoToHome() {
        val navController = getNavController()

        navController.navigate(R.id.controllerHome)
    }

    override fun onGoToBids(id: Long, sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ControllerDettagliAsta::class -> {
                val action =
                    ControllerDettagliAstaDirections.actionControllerDettagliAstaToControllerOfferte(
                        id
                    )
                navController.navigate(action)
            }

            ViewHolderAstaCreata::class -> {
                val action =
                    ControllerAsteCreateDirections.actionControllerAsteCreateToControllerOfferte(id)
                navController.navigate(action)
            }
        }
    }

    override fun onRefresh(id: Long, sender: KClass<*>) {
        val navController = getNavController()

        when (sender) {
            ControllerDettagliAsta::class -> {
                navController.popBackStack()
                val action =
                    ControllerDettagliAstaDirections.actionControllerDettagliAstaSelf(id)
                navController.navigate(action)
            }

            ViewHolderOfferta::class -> {
                navController.popBackStack()
                val action =
                    ControllerOfferteDirections.actionControllerOfferteSelf(id)
                navController.navigate(action)
            }

            ViewHolderAstaCreata::class -> {
                navController.popBackStack()
                val action =
                    ControllerAsteCreateDirections.actionControllerAsteCreateSelf()
                navController.navigate(action)
            }
        }
    }

    override fun onGoToParticipation() {
        val navController = getNavController()

        navController.navigate(R.id.controllerPartecipazioni)
    }

    override fun onGoToCreatedAuctions() {
        val navController = getNavController()

        navController.navigate(R.id.controllerAsteCreate)
    }
}