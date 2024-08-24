package com.iasdietideals24.dietideals24.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class DietiDeals24Activity<bindingType : ViewBinding> : AppCompatActivity() {

    /**
     * Una istanza di una classe generata a tempo di compilazione dai file XML delle viste, che
     * contiene tutti gli elementi della vista come campi della classe stessa.
     */
    protected lateinit var binding: bindingType

    /**
     * Effettua il binding delle classi generate dalla build.
     */
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val classe =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val metodo = classe.getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        binding = metodo.invoke(null, layoutInflater) as bindingType
        val view = binding.root
        setContentView(view)
    }
}