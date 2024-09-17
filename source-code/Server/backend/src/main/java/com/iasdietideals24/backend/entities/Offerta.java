package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Offerta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offerta_id_seq")
    private Long idOfferta;

    @NonNull
    private LocalDate dataInvio;

    @NonNull
    private LocalTime oraInvio;

    @NonNull
    private BigDecimal valore;

    // AllArgsConstructor
    protected Offerta(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore) {
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.valore = valore;
    }

    @Override
    public String toString() {
        return "Offerta(idOfferta=" + this.getIdOfferta() + ", dataInvio=" + this.getDataInvio() + ", oraInvio=" + this.getOraInvio() + ", valore=" + this.getValore() + ")";
    }
}
