package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AiutoBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton

class ControllerAiuto : Controller<AiutoBinding>() {

    private var listenerBackButton: OnBackButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.aiutoPulsanteIndietro.setOnClickListener {
            listenerBackButton?.onBackButton()
        }

        binding.aiutoPulsante1.setOnClickListener {
            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(getString(R.string.aiuto_titoloAstaTempoFisso))
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setMessage(getString(R.string.aiuto_astaTempoFisso))
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante2.setOnClickListener {
            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(getString(R.string.aiuto_titoloAstaSilenziosa))
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setMessage(getString(R.string.aiuto_astaSilenziosa))
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante3.setOnClickListener {
            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(getString(R.string.aiuto_titoloAstaInversa))
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setMessage(getString(R.string.aiuto_astaInversa))
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante4.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante4, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante4)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante5.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante5, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante5)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante6.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante6, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante6)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante7.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante7, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante7)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante8.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante8, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante8)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante9.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante9, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante9)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante10.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante10, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante10)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }

        binding.aiutoPulsante11.setOnClickListener {
            val viewInflated: View = LayoutInflater.from(context)
                .inflate(R.layout.popup_pulsante11, view as ViewGroup?, false)

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.aiuto_pulsante11)
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }
}