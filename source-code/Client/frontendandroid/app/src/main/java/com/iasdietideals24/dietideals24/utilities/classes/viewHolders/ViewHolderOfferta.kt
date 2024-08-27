package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.OffertaBinding
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.interfaces.OnRefreshFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class ViewHolderOfferta(private val binding: OffertaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var layoutListener: OnGoToDetails? = null
    private var immagineListener: OnGoToProfile? = null
    private var refreshListener: OnRefreshFragment? = null

    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            layoutListener = context
        }
        if (context is OnGoToProfile) {
            immagineListener = context
        }
        if (context is OnRefreshFragment) {
            refreshListener = context
        }
    }

    fun bind(currentOfferta: OffertaRicevuta, resources: Resources) {
        binding.offertaNome.text = currentOfferta.nomeOfferente
        if (currentOfferta.immagineOfferente.isNotEmpty()) {
            binding.offertaImmagine.load(currentOfferta.immagineOfferente) {
                crossfade(true)
            }
            binding.offertaImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        binding.offertaValoreOfferta.text =
            resources.getString(R.string.placeholder_prezzo, currentOfferta.offerta.toString())

        val tempoFa = when {
            LocalDate.now() == currentOfferta.dataInvio -> currentOfferta.oraInvio.toString()
            else -> currentOfferta.dataInvio.toLocalStringShort() + " " + currentOfferta.oraInvio.toString()
        }

        binding.offertaTempo.text = resources.getString(R.string.placeholder, tempoFa)

        if (currentOfferta.accettata == null) {
            binding.offertaLinearLayout1.setOnClickListener {
                layoutListener?.onGoToDetails(currentOfferta.idAsta, this::class)
            }

            binding.offertaImmagine.setOnClickListener {
                immagineListener?.onGoToProfile(currentOfferta.idOfferente, this::class)
            }

            binding.offertaPulsanteAccetta.setOnClickListener {
                MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                    .setTitle(R.string.offerta_confermaAccetta)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        clickAccetta(currentOfferta.idAsta, currentOfferta.idOfferta, resources)
                    }
                    .setNegativeButton(R.string.annulla) { _, _ -> }
                    .show()
            }

            binding.offertaPulsanteRifiuta.setOnClickListener {
                MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                    .setTitle(R.string.offerta_confermaRifiuto)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        clickRifiuta(currentOfferta.idAsta, currentOfferta.idOfferta, resources)
                    }
                    .setNegativeButton(R.string.annulla) { _, _ -> }
                    .show()
            }

        } else {
            if (currentOfferta.accettata == false)
                binding.offertaLinearLayout1.setBackgroundColor(
                    resources.getColor(
                        R.color.grigioChiaro,
                        null
                    )
                )

            binding.offertaPulsanteAccetta.visibility = View.GONE
            binding.offertaPulsanteRifiuta.visibility = View.GONE
        }
    }

    private fun clickAccetta(idAsta: Long, idOfferta: Long, resources: Resources) {
        var returned: Boolean? = null

        val call =
            APIController.instance.accettaOfferta(idOfferta)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    returned = response.body()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                throw t
            }
        })

        when (returned) {
            null -> {
                Snackbar.make(
                    itemView,
                    R.string.apiError,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }

            true -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaAccettata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                refreshListener?.onRefreshFragment(idAsta, this::class)
            }

            false -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaNonAccettata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private fun clickRifiuta(idAsta: Long, idOfferta: Long, resources: Resources) {
        var returned: Boolean? = null

        val call =
            APIController.instance.rifiutaOfferta(idOfferta)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    returned = response.body()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                throw t
            }
        })

        when (returned) {
            null -> {
                Snackbar.make(
                    itemView,
                    R.string.apiError,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }

            true -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaRifiutata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                refreshListener?.onRefreshFragment(idAsta, this::class)
            }

            false -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaNonRifiutata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    fun cleanListeners() {
        layoutListener = null
        immagineListener = null
        refreshListener = null
    }
}