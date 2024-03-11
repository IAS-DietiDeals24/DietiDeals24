package com.iasdietideals24.dietideals24.registrazione

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import com.iasdietideals24.dietideals24.R

class FrameInfoPassword(private val controller: ControllerRegistrazione) {
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var pulsanteChiudi: ImageButton

    init {
        val layoutPopup = gonfiaLayout()

        val finestraPopup = creaFinestraPopup(layoutPopup)

        trovaElementiInterfaccia(layoutPopup)

        // Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        // una cortina nera come foreground.
        // Viene resa invisibile quando la pagina di registrazione è creata e quando è aperto il
        // popup.
        // Viene resa invisibile quando il popup è chiuso.
        impostaTrasparenzaCortina(150)

        impostaEventiClick(finestraPopup)

        finestraPopup.showAtLocation(layoutPopup, Gravity.CENTER, 0, 0)
    }

    private fun gonfiaLayout(): View {
        val servizioInflater =
            controller.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val radiceLayout =
            controller.findViewById<ConstraintLayout>(R.id.infoPassword_constraintLayout)

        return servizioInflater.inflate(R.layout.infopassword, radiceLayout)
    }

    private fun creaFinestraPopup(popupDaCreare: View): PopupWindow {
        val larghezzaFinestra = LinearLayout.LayoutParams.WRAP_CONTENT
        val altezzaFinestra = LinearLayout.LayoutParams.WRAP_CONTENT
        val chiusuraConTapEsterno = false

        return PopupWindow(popupDaCreare, larghezzaFinestra, altezzaFinestra, chiusuraConTapEsterno)
    }

    private fun trovaElementiInterfaccia(layoutPopupCreato: View) {
        constraintLayout = controller.findViewById(R.id.registrazioneFase1_constraintLayout)
        pulsanteChiudi = layoutPopupCreato.findViewById(R.id.infoPassword_pulsanteChiudi)
    }

    private fun impostaTrasparenzaCortina(alpha: Int) {
        constraintLayout.foreground.alpha = alpha
    }

    private fun impostaEventiClick(finestraPopupCreata: PopupWindow) {
        pulsanteChiudi.setOnClickListener {
            finestraPopupCreata.dismiss()

            impostaTrasparenzaCortina(0)
        }
    }
}