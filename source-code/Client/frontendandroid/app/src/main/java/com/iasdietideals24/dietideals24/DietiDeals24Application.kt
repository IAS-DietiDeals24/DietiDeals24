package com.iasdietideals24.dietideals24

import android.app.Application
import com.iasdietideals24.dietideals24.utilities.kscripts.adapterModule
import com.iasdietideals24.dietideals24.utilities.kscripts.dataStoreModule
import com.iasdietideals24.dietideals24.utilities.kscripts.facebookModule
import com.iasdietideals24.dietideals24.utilities.kscripts.pagingSourceModule
import com.iasdietideals24.dietideals24.utilities.kscripts.repositoryModule
import com.iasdietideals24.dietideals24.utilities.kscripts.serviceModule
import com.iasdietideals24.dietideals24.utilities.kscripts.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DietiDeals24Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Avvia Koin caricando i seguenti moduli
        startKoin {
            androidContext(this@DietiDeals24Application)
            modules(
                serviceModule,
                repositoryModule,
                pagingSourceModule,
                adapterModule,
                viewModelModule,
                facebookModule,
                dataStoreModule
            )
        }
    }
}