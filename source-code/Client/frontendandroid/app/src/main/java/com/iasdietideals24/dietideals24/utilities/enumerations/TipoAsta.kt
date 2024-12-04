package com.iasdietideals24.dietideals24.utilities.enumerations

enum class TipoAsta {
    INVERSA, TEMPO_FISSO, SILENZIOSA;

    companion object {
        fun getEnum(value: String): TipoAsta {
            return when (value) {
                "AstaInversa" -> INVERSA
                "AstaTempoFisso" -> TEMPO_FISSO
                "AstaSilenziosa" -> SILENZIOSA
                else -> throw IllegalArgumentException("Valore non valido: $value")
            }
        }
    }
}