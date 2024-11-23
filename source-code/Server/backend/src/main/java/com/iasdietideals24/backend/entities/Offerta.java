package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Check;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "offerta")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Check(constraints = "data_invio <= NOW() AND valore > 0")
public abstract class Offerta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offerta_id_seq")
    @Column(name = "id_offerta", nullable = false)
    private Long idOfferta;

    @NonNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_invio", nullable = false)
    private LocalDate dataInvio;

    @NonNull
    @Temporal(TemporalType.TIME)
    @Column(name = "ora_invio", nullable = false)
    private LocalTime oraInvio;

    @NonNull
    @Column(name = "valore", nullable = false, scale = 2, precision = 10)
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
