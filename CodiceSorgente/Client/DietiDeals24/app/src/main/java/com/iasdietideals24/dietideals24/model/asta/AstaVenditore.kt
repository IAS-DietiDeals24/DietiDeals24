package com.iasdietideals24.dietideals24.model.asta

import com.iasdietideals24.dietideals24.model.Notifica
import com.iasdietideals24.dietideals24.model.account.Account
import java.sql.Date
import java.sql.Time

abstract class AstaVenditore protected constructor(
    categoria: String,
    nome: String,
    descrizione: String,
    dataScadenza: Date,
    oraScadenza: Time,
    immaginiList: MutableList<String>,
    notificheAssociateList: MutableList<Notifica>,

    private var proprietario: Account

) : Asta(
    categoria,
    nome,
    descrizione,
    dataScadenza,
    oraScadenza,
    immaginiList,
    notificheAssociateList
) {

    fun getProprietario(): Account {
        return proprietario
    }

    fun setProprietario(proprietario: Account) {
        this.proprietario = proprietario
    }
}