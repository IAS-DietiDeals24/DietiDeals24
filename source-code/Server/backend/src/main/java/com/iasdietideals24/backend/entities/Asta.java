package com.iasdietideals24.backend.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
public abstract class Asta {
    @NonNull
    private String categoria;

    @NonNull
    private String nome;

    @NonNull
    private String descrizione;

    @NonNull
    private LocalDate dataScadenza;

    @NonNull
    private LocalTime oraScadenza;

    private byte[] immagine;

    @Setter(AccessLevel.NONE)
    private Set<Notifica> notificheAssociate;

    // AllArgsConstructor
    public Asta(@NonNull String categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine) {
        this.categoria = categoria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;
    }

    // Metodi per notificheAssociate
    public void addNotificaAssociata(Notifica notificaDaAggiungere) {
        if (this.notificheAssociate == null)
            this.notificheAssociate = new HashSet<Notifica>();

        this.notificheAssociate.add(notificaDaAggiungere);
    }

    public void removeNotificaAssociata(Notifica notificaDaRimuovere) {
        this.notificheAssociate.remove(notificaDaRimuovere);

        if (this.notificheAssociate.isEmpty())
            this.notificheAssociate = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asta)) return false;
        Asta asta = (Asta) o;
        return Objects.equals(this.categoria, asta.getCategoria()) && Objects.equals(this.nome, asta.getNome()) && Objects.equals(this.descrizione, asta.getDescrizione()) && Objects.equals(this.dataScadenza, asta.getDataScadenza()) && Objects.equals(this.oraScadenza, asta.getOraScadenza()) && Objects.equals(this.immagine, asta.getImmagine()) && Objects.equals(this.notificheAssociate, asta.getNotificheAssociate());
    }
}
