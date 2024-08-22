package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails

class AdapterHome(
    private val aste: Array<DatiAnteprimaAsta>,
    private val resources: Resources
) : RecyclerView.Adapter<AnteprimaAsta>() {

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int =
        resources.getInteger(android.R.integer.config_shortAnimTime)
    private var listener: OnGoToDetails? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnteprimaAsta {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.asta, parent, false)

        if (parent.context is OnGoToDetails) {
            listener = parent.context as OnGoToDetails
        }

        return AnteprimaAsta(view)
    }

    override fun getItemCount(): Int {
        return aste.size
    }

    override fun getItemId(position: Int): Long {
        return aste[position]._id
    }

    override fun onBindViewHolder(holder: AnteprimaAsta, position: Int) {
        val currentAsta = aste[position]

        when (currentAsta._tipoAsta) {
            "Inversa" -> {
                holder.tipo.text = resources.getString(R.string.tipoAsta_astaInversa)
                holder.messaggio.text = resources.getString(R.string.dettagliAsta_testoOfferta2)
            }

            "Silenziosa" -> {
                holder.tipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
                holder.messaggio.text = resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            "Tempo fisso" -> {
                holder.tipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
                holder.messaggio.text = resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            else -> {
                holder.tipo.text = ""
                holder.messaggio.text = ""
            }
        }

        holder.data.text =
            currentAsta._dataScadenza.toLocalStringShort()
        holder.ora.text = currentAsta._oraScadenza.toString()

        holder.foto.load(currentAsta._foto) {
            crossfade(true)
        }
        holder.nome.text = currentAsta._nome
        holder.offerta.text = currentAsta._offerta.toString()
        holder.modifica.visibility = android.view.View.GONE
        holder.elimina.visibility = android.view.View.GONE
        holder.offerte.visibility = android.view.View.GONE

        holder.foto.setOnClickListener {
            currentAnimator?.cancel()

            holder.fotoZoomata.load(currentAsta._foto) {
                crossfade(true)
            }

            val startBoundsInt = Rect()
            val finalBoundsInt = Rect()
            val globalOffset = Point()

            holder.foto.getGlobalVisibleRect(startBoundsInt)
            holder.fotoZoomata.getGlobalVisibleRect(finalBoundsInt, globalOffset)
            startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
            finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

            val startBounds = RectF(startBoundsInt)
            val finalBounds = RectF(finalBoundsInt)

            val startScale: Float
            if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
                startScale = startBounds.height() / finalBounds.height()
                val startWidth: Float = startScale * finalBounds.width()
                val deltaWidth: Float = (startWidth - startBounds.width()) / 2
                startBounds.left -= deltaWidth.toInt()
                startBounds.right += deltaWidth.toInt()
            } else {
                startScale = startBounds.width() / finalBounds.width()
                val startHeight: Float = startScale * finalBounds.height()
                val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
                startBounds.top -= deltaHeight.toInt()
                startBounds.bottom += deltaHeight.toInt()
            }

            holder.foto.alpha = 0f

            holder.fotoZoomata.visibility = View.VISIBLE

            holder.fotoZoomata.pivotX = 0f
            holder.fotoZoomata.pivotY = 0f

            currentAnimator = AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        holder.fotoZoomata,
                        View.X,
                        startBounds.left,
                        finalBounds.left
                    )
                ).apply {
                    with(
                        ObjectAnimator.ofFloat(
                            holder.fotoZoomata,
                            View.Y,
                            startBounds.top,
                            finalBounds.top
                        )
                    )
                    with(ObjectAnimator.ofFloat(holder.fotoZoomata, View.SCALE_X, startScale, 1f))
                    with(ObjectAnimator.ofFloat(holder.fotoZoomata, View.SCALE_Y, startScale, 1f))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        currentAnimator = null
                    }
                })
                start()
            }

            holder.fotoZoomata.setOnClickListener {
                currentAnimator?.cancel()

                currentAnimator = AnimatorSet().apply {
                    play(
                        ObjectAnimator.ofFloat(
                            holder.fotoZoomata,
                            View.X,
                            startBounds.left
                        )
                    ).apply {
                        with(ObjectAnimator.ofFloat(holder.fotoZoomata, View.Y, startBounds.top))
                        with(ObjectAnimator.ofFloat(holder.fotoZoomata, View.SCALE_X, startScale))
                        with(ObjectAnimator.ofFloat(holder.fotoZoomata, View.SCALE_Y, startScale))
                    }
                    duration = shortAnimationDuration.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {

                        override fun onAnimationEnd(animation: Animator) {
                            holder.foto.alpha = 1f
                            holder.fotoZoomata.visibility = View.GONE
                            currentAnimator = null
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            holder.foto.alpha = 1f
                            holder.fotoZoomata.visibility = View.GONE
                            currentAnimator = null
                        }
                    })
                    start()
                }
            }
        }

        holder.layout.setOnClickListener {
            listener?.onGoToDetails(holder.itemId, this::class)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        listener = null
    }

}
