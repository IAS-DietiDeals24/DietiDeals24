package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Asta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asta_id_seq")
    @NonNull
    private Long idAsta;

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

    @OneToMany(mappedBy = "astaAssociata", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Notifica> notificheAssociate = new HashSet<Notifica>();

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
        this.notificheAssociate.add(notificaDaAggiungere);
    }

    public void removeNotificaAssociata(Notifica notificaDaRimuovere) {
        this.notificheAssociate.remove(notificaDaRimuovere);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asta)) return false;
        Asta asta = (Asta) o;
        return Objects.equals(this.categoria, asta.getCategoria()) && Objects.equals(this.nome, asta.getNome()) && Objects.equals(this.descrizione, asta.getDescrizione()) && Objects.equals(this.dataScadenza, asta.getDataScadenza()) && Objects.equals(this.oraScadenza, asta.getOraScadenza()) && Objects.equals(this.immagine, asta.getImmagine()) && Objects.equals(this.notificheAssociate, asta.getNotificheAssociate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAsta, categoria, nome, descrizione, dataScadenza, oraScadenza, Arrays.hashCode(immagine), notificheAssociate);
    }

    @Override
    public String toString() {
        Iterator<Notifica> itrNotificaAssociata = this.getNotificheAssociate().iterator();
        StringBuilder listIdNotificheAssociate = new StringBuilder();
        listIdNotificheAssociate.append("[");
        while (itrNotificaAssociata.hasNext()) {
            listIdNotificheAssociate.append(itrNotificaAssociata.next().getIdNotifica()).append(", ");
        }
        listIdNotificheAssociate.append("]");

        return "Asta(idAsta=" + this.getIdAsta() + ", categoria=" + this.getCategoria() + ", nome=" + this.getNome() + ", descrizione=" + this.getDescrizione() + ", dataScadenza=" + this.getDataScadenza() + ", oraScadenza=" + this.getOraScadenza() + ", immagine=" + java.util.Arrays.toString(this.getImmagine()) + ", notificheAssociate=" + listIdNotificheAssociate + ")";
    }
}
