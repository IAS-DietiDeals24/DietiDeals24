package com.iasdietideals24.dietideals24.activities

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAsta
import com.iasdietideals24.dietideals24.controller.ControllerDettagliAstaDirections
import com.iasdietideals24.dietideals24.controller.ControllerHomeDirections
import com.iasdietideals24.dietideals24.controller.ControllerNotificheDirections
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import kotlin.reflect.KClass

class Home : AppCompatActivity(R.layout.activity_home), OnGoToDetails, OnGoToProfile {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_home_fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.activity_home_bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)

        if (CurrentUser.id == 0L) {
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.menu.getItem(2).isEnabled = false
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
        }
    }
}