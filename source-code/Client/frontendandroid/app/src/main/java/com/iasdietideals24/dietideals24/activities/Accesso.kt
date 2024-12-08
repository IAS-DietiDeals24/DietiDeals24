package com.iasdietideals24.dietideals24.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.iasdietideals24.dietideals24.databinding.ActivityAccessoBinding
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class Accesso : DietiDeals24Activity<ActivityAccessoBinding>() {

    // Repositories
    private val authRepository: AuthRepository by inject()
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()
    private val profiloRepository: ProfiloRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.accesso) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.data?.scheme == "ias" &&
            intent.data?.host == "com.iasdietideals24.dietideals24" &&
            intent.data?.path == "/signin"
        ) {

            val code = intent.data?.getQueryParameter("code") ?: ""

            lifecycleScope.launch {
                val tokens = withContext(Dispatchers.IO) {
                    authRepository.recuperaToken(
                        code,
                        "ias://com.iasdietideals24.dietideals24/signin"
                    )
                }

                CurrentUser.rToken = tokens.refreshToken
                CurrentUser.aToken = tokens.authToken

                if (CurrentUser.rToken != "" && CurrentUser.aToken != "") {
                    withContext(Dispatchers.IO) {
                        authRepository.scriviRefreshToken(CurrentUser.rToken)
                    }

                    val userEmail = JWT.getUserEmail(CurrentUser.aToken)

                    val account = withContext(Dispatchers.IO) {
                        when (CurrentUser.tipoAccount) {
                            TipoAccount.COMPRATORE -> compratoreRepository.accediCompratore(
                                userEmail
                            )

                            TipoAccount.VENDITORE -> venditoreRepository.accediVenditore(userEmail)

                            else -> CompratoreDto()
                        }
                    }

                    CurrentUser.id = account.idAccount

                    val profilo = withContext(Dispatchers.IO) {
                        profiloRepository.caricaProfilo(account.profiloShallow.nomeUtente)
                    }

                    if (profilo.nomeUtente == "") {
                        startActivity(
                            Intent(baseContext, Registrazione::class.java)
                                .putExtra("skip", true)
                                .putExtra("email", userEmail)
                        )

                        finishAffinity()
                    } else {
                        startActivity(
                            Intent(baseContext, Home::class.java)
                        )

                        finishAffinity()
                    }
                }
            }
        }
    }
}