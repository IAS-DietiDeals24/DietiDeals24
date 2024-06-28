package com.iasdietideals24.dietideals24.model

import com.iasdietideals24.dietideals24.model.account.Account
import java.sql.Date

class Profilo(
    private var nomeUtente: String,
    private var profilePicture: String,
    private var nome: String,
    private var cognome: String,
    private var dataNascita: Date,
    private var areaGeografica: String,
    private var genere: String,
    private var biografia: String,
    private var linkPersonale: String,
    private var linkInstagram: String,
    private var linkFacebook: String,
    private var linkGithub: String,
    private var linkX: String,
    private var accountList: MutableList<Account>
) {
    fun getNomeUtente(): String {
        return nomeUtente
    }

    fun getProfilePicture(): String {
        return profilePicture
    }

    fun getNome(): String {
        return nome
    }

    fun getCognome(): String {
        return cognome
    }

    fun getDataNascita(): Date {
        return dataNascita
    }

    fun getAreaGeografica(): String {
        return areaGeografica
    }

    fun getGenere(): String {
        return genere
    }

    fun getBiografia(): String {
        return biografia
    }

    fun getLinkPersonale(): String {
        return linkPersonale
    }

    fun getLinkInstagram(): String {
        return linkInstagram
    }

    fun getLinkFacebook(): String {
        return linkFacebook
    }

    fun getLinkGithub(): String {
        return linkGithub
    }

    fun getLinkX(): String {
        return linkX
    }

    fun getAccountList(): MutableList<Account> {
        return accountList
    }

    fun setNomeUtente(nomeUtente: String) {
        this.nomeUtente = nomeUtente
    }

    fun setProfilePicture(profilePicture: String) {
        this.profilePicture = profilePicture
    }

    fun setNome(nome: String) {
        this.nome = nome
    }

    fun setCognome(cognome: String) {
        this.cognome = cognome
    }

    fun setDataNascita(dataNascita: Date) {
        this.dataNascita = dataNascita
    }

    fun setAreaGeografica(areaGeografica: String) {
        this.areaGeografica = areaGeografica
    }

    fun setGenere(genere: String) {
        this.genere = genere
    }

    fun setBiografia(biografia: String) {
        this.biografia = biografia
    }

    fun setLinkPersonale(linkPersonale: String) {
        this.linkPersonale = linkPersonale
    }

    fun setLinkInstagram(linkInstagram: String) {
        this.linkInstagram = linkInstagram
    }

    fun setLinkFacebook(linkFacebook: String) {
        this.linkFacebook = linkFacebook
    }

    fun setLinkGithub(linkGithub: String) {
        this.linkGithub = linkGithub
    }

    fun setLinkX(linkX: String) {
        this.linkX = linkX
    }

    fun setAccountList(accountList: MutableList<Account>) {
        this.accountList = accountList
    }

    fun addAccount(account: Account): Boolean {
        return accountList.add(account)
    }

    fun removeAccount(account: Account): Boolean {
        return accountList.remove(account)
    }
}