package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R

class Notifica(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val _layout: LinearLayout

    val layout: LinearLayout
        get() = _layout

    private val _mittente: MaterialTextView

    val mittente: MaterialTextView
        get() = _mittente

    private val _immagineMittente: ShapeableImageView

    val immagineMittente: ShapeableImageView
        get() = _immagineMittente

    private val _messaggio: MaterialTextView

    val messaggio: MaterialTextView
        get() = _messaggio

    private val _dataInvio: MaterialTextView

    val dataInvio: MaterialTextView
        get() = _dataInvio

    init {
        _layout = itemView.findViewById(R.id.notifica_linearLayout1)
        _mittente = itemView.findViewById(R.id.notifica_nome)
        _immagineMittente = itemView.findViewById(R.id.notifica_immagine)
        _messaggio = itemView.findViewById(R.id.notifica_testo)
        _dataInvio = itemView.findViewById(R.id.notifica_tempo)
    }
}