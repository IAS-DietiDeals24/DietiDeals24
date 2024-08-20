package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R

class AnteprimaAsta(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val _layout: LinearLayout

    val layout : LinearLayout
        get() = _layout

    private val _tipo: MaterialTextView

    val tipo : MaterialTextView
        get() = _tipo

    private val _data: MaterialTextView

    val data : MaterialTextView
        get() = _data

    private val _foto: ShapeableImageView

    val foto : ShapeableImageView
        get() = _foto

    private val _fotoZoomata: ShapeableImageView

    val fotoZoomata : ShapeableImageView
        get() = _fotoZoomata

    private val _nome: MaterialTextView

    val nome : MaterialTextView
        get() = _nome

    private val _messaggio: MaterialTextView

    val messaggio : MaterialTextView
        get() = _messaggio

    private val _offerta: MaterialTextView

    val offerta : MaterialTextView
        get() = _offerta

    private val _modifica: ImageButton

    val modifica : ImageButton
        get() = _modifica

    private val _elimina: ImageButton

    val elimina : ImageButton
        get() = _elimina

    private val _offerte: ImageButton

    val offerte : ImageButton
        get() = _offerte


    init {
        _layout = itemView.findViewById(R.id.asta_linearLayout3)
        _tipo = itemView.findViewById(R.id.asta_tipo)
        _data = itemView.findViewById(R.id.asta_dataScadenza)
        _foto = itemView.findViewById(R.id.asta_immagine)
        _fotoZoomata = itemView.findViewById(R.id.home_zoom)
        _nome = itemView.findViewById(R.id.asta_nome)
        _offerta = itemView.findViewById(R.id.asta_offerta)
        _messaggio = itemView.findViewById(R.id.asta_messaggio)
        _modifica = itemView.findViewById(R.id.asta_modificaAsta)
        _elimina = itemView.findViewById(R.id.asta_eliminaAsta)
        _offerte = itemView.findViewById(R.id.asta_elencoOfferte)
    }
}