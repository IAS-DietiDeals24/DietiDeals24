package com.iasdietideals24.dietideals24.utilities.kscripts

import com.facebook.CallbackManager.Factory.create
import com.iasdietideals24.dietideals24.model.ModelAccesso
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.model.ModelAsteCreate
import com.iasdietideals24.dietideals24.model.ModelHome
import com.iasdietideals24.dietideals24.model.ModelNotifiche
import com.iasdietideals24.dietideals24.model.ModelPartecipazioni
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterAsteCreate
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterOfferte
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterPartecipazioni
import com.iasdietideals24.dietideals24.utilities.comparators.AstaDtoComparator
import com.iasdietideals24.dietideals24.utilities.comparators.NotificaDtoComparator
import com.iasdietideals24.dietideals24.utilities.comparators.OffertaDtoComparator
import com.iasdietideals24.dietideals24.utilities.paging.AstaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.NotificaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.OffertaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.OffertaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.OffertaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.NotificaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.services.AstaInversaService
import com.iasdietideals24.dietideals24.utilities.services.AstaSilenziosaService
import com.iasdietideals24.dietideals24.utilities.services.AstaTempoFissoService
import com.iasdietideals24.dietideals24.utilities.services.CategoriaAstaService
import com.iasdietideals24.dietideals24.utilities.services.CompratoreService
import com.iasdietideals24.dietideals24.utilities.services.NotificaService
import com.iasdietideals24.dietideals24.utilities.services.OffertaInversaService
import com.iasdietideals24.dietideals24.utilities.services.OffertaSilenziosaService
import com.iasdietideals24.dietideals24.utilities.services.OffertaTempoFissoService
import com.iasdietideals24.dietideals24.utilities.services.ProfiloService
import com.iasdietideals24.dietideals24.utilities.services.VenditoreService
import com.iasdietideals24.dietideals24.utilities.tools.RetrofitController
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val serviceModule = module {
    single<AstaInversaService> { RetrofitController.service() }
    single<AstaSilenziosaService> { RetrofitController.service() }
    single<AstaTempoFissoService> { RetrofitController.service() }
    single<CompratoreService> { RetrofitController.service() }
    single<NotificaService> { RetrofitController.service() }
    single<OffertaInversaService> { RetrofitController.service() }
    single<OffertaSilenziosaService> { RetrofitController.service() }
    single<OffertaTempoFissoService> { RetrofitController.service() }
    single<ProfiloService> { RetrofitController.service() }
    single<VenditoreService> { RetrofitController.service() }
    single<CategoriaAstaService> { RetrofitController.service() }
}

val repositoryModule = module {
    single { AstaInversaRepository(get()) }
    single { AstaSilenziosaRepository(get()) }
    single { AstaTempoFissoRepository(get()) }
    single { CompratoreRepository(get()) }
    single { NotificaRepository(get()) }
    single { OffertaInversaRepository(get()) }
    single { OffertaSilenziosaRepository(get()) }
    single { OffertaTempoFissoRepository(get()) }
    single { ProfiloRepository(get()) }
    single { VenditoreRepository(get()) }
    single { CategoriaAstaRepository(get()) }
}

val pagingSourceModule = module {
    factory { params ->
        AstaInversaPagingSource(
            get(),
            params.get(),
            params.get(),
            params.get(),
            params.get()
        )
    }
    factory { params ->
        AstaSilenziosaPagingSource(
            get(),
            params.get(),
            params.get(),
            params.get(),
            params.get()
        )
    }
    factory { params ->
        AstaTempoFissoPagingSource(
            get(),
            params.get(),
            params.get(),
            params.get(),
            params.get()
        )
    }
    factory { params -> NotificaPagingSource(get(), params.get()) }
    factory { params -> OffertaInversaPagingSource(get(), params.get()) }
    factory { params -> OffertaSilenziosaPagingSource(get(), params.get()) }
    factory { params -> OffertaTempoFissoPagingSource(get(), params.get()) }
}

val adapterModule = module {
    factory { params -> AdapterAsteCreate(AstaDtoComparator, params.get()) }
    factory { params -> AdapterHome(AstaDtoComparator, params.get()) }
    factory { params -> AdapterNotifiche(NotificaDtoComparator, params.get()) }
    factory { params -> AdapterPartecipazioni(AstaDtoComparator, params.get()) }
    factory { params -> AdapterOfferte(OffertaDtoComparator, params.get()) }
}

val viewModelModule = module {
    viewModel { ModelAccesso() }
    viewModel { ModelRegistrazione(get(), get(), get()) }
    viewModel { ModelAsta(get(), get(), get()) }
    viewModel { ModelProfilo() }
    viewModel { ModelAsteCreate(get(), get(), get()) }
    viewModel { ModelHome(get(), get(), get()) }
    viewModel { ModelNotifiche(get()) }
    viewModel { ModelPartecipazioni(get(), get(), get()) }
}

val facebookModule = module {
    factory { create() }
}