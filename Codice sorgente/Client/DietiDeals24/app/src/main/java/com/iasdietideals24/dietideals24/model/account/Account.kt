package com.iasdietideals24.dietideals24.model.account

import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.Profilo

abstract class Account(
    private var email: String,
    private var password: String,
    private var profilo: Profilo,
    private var notificheInviate: MutableList<Notifica>,
    private var notificheRicevute: MutableList<Notifica>
) {
    fun getEmail(): String {
        return email
    }

    fun getPassword(): String {
        return password
    }

    fun getProfilo(): Profilo {
        return profilo
    }

    fun getNotificheInviate(): MutableList<Notifica> {
        return notificheInviate
    }

    fun getNotificheRicevute(): MutableList<Notifica> {
        return notificheRicevute
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setProfilo(profilo: Profilo) {
        this.profilo = profilo
    }

    fun setNotificheInviate(notificheInviate: MutableList<Notifica>) {
        this.notificheInviate = notificheInviate
    }

    fun setNotificheRicevute(notificheRicevute: MutableList<Notifica>) {
        this.notificheRicevute = notificheRicevute
    }

    fun addNotificaInviate(notifica: Notifica) {
        notificheInviate.add(notifica)
    }

    fun addNotificaRicevute(notifica: Notifica) {
        notificheRicevute.add(notifica)
    }

    fun removeNotificaInviate(notifica: Notifica) {
        notificheInviate.remove(notifica)
    }

    fun removeNotificaRicevute(notifica: Notifica) {
        notificheRicevute.remove(notifica)
    }
}
