package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Offerta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offerta_id_seq")
    @NonNull
    private Long idOfferta;

    @NonNull
    private LocalDate dataInvio;

    @NonNull
    private LocalTime oraInvio;

    @NonNull
    private BigDecimal valore;

    // AllArgsConstructor
    public Offerta(@NonNull LocalDate dataInvio, @NonNull LocalTime oraInvio, @NonNull BigDecimal valore) {
        this.dataInvio = dataInvio;
        this.oraInvio = oraInvio;
        this.valore = valore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offerta)) return false;
        Offerta offerta = (Offerta) o;
        return Objects.equals(this.dataInvio, offerta.getDataInvio()) && Objects.equals(this.oraInvio, offerta.getOraInvio()) && Objects.equals(this.valore, offerta.getValore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOfferta, dataInvio, oraInvio, valore);
    }

    @Override
    public String toString() {
        return "Offerta(idOfferta=" + this.getIdOfferta() + ", dataInvio=" + this.getDataInvio() + ", oraInvio=" + this.getOraInvio() + ", valore=" + this.getValore() + ")";
    }
}
