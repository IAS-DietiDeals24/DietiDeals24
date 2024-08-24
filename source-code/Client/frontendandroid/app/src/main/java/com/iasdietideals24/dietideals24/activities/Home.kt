package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAsta
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAstaDirections
import com.iasdietideals24.dietideals24.controller.ControllerDettagliProfilo
import com.iasdietideals24.dietideals24.controller.ControllerDettagliProfiloDirections
import com.iasdietideals24.dietideals24.controller.ControllerHomeDirections
import com.iasdietideals24.dietideals24.controller.ControllerModificaProfilo
import com.iasdietideals24.dietideals24.controller.ControllerModificaProfiloDirections
import com.iasdietideals24.dietideals24.controller.ControllerNotificheDirections
import com.iasdietideals24.dietideals24.controller.ControllerProfiloDirections
import com.iasdietideals24.dietideals24.databinding.ActivityHomeBinding
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentOpenUrl
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import kotlin.reflect.KClass

class Home : DietiDeals24Activity<ActivityHomeBinding>(), OnGoToDetails, OnGoToProfile,
    OnFragmentEditButton, OnFragmentChangeActivity, OnFragmentOpenUrl {

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

    override fun onGoToDetails(id: Long, sender: KClass<*>) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            sender == AdapterHome::class -> {
                val action =
                    ControllerHomeDirections.actionControllerHomeToControllerDettagliAsta(id)
                navController.navigate(action)
            }

            sender == AdapterNotifiche::class -> {
                val action =
                    ControllerNotificheDirections.actionControllerNotificheToControllerDettagliAsta(
                        id
                    )
                navController.navigate(action)
            }
        }
    }

    override fun onGoToProfile(id: Long, sender: KClass<*>) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            sender == AdapterNotifiche::class -> {
                val action =
                    ControllerNotificheDirections.actionControllerNotificheToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            sender == ControllerDettagliAsta::class -> {
                val action =
                    ControllerDettagliAstaDirections.actionControllerDettagliAstaToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            sender == ControllerDettagliProfilo::class -> {
                val action =
                    ControllerProfiloDirections.actionControllerProfiloToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }

            sender == ControllerModificaProfilo::class -> {
                val action =
                    ControllerModificaProfiloDirections.actionControllerModificaProfiloToControllerDettagliProfilo(
                        id
                    )
                navController.navigate(action)
            }
        }
    }

    override fun onFragmentEditButton(id: Long, sender: KClass<*>) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        when {
            sender == ControllerDettagliProfilo::class -> {
                val action =
                    ControllerDettagliProfiloDirections.actionControllerDettagliProfiloToControllerModificaProfilo()
                navController.navigate(action)
            }
        }
    }

    override fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>) {
        startActivity(Intent(baseContext, activity))
        finishAffinity()
    }

    override fun onFragmentOpenUrl(externalUrl: String) {
        var url = externalUrl

        if (url.startsWith("http://") == false
            && url.startsWith("https://") == false
        ) {
            url = "http://$url"
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}