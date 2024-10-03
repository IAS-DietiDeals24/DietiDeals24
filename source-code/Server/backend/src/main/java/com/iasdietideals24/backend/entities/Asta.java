package com.iasdietideals24.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Asta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asta_id_seq")
    private Long idAsta;

    @NonNull
    private String nome;

    @NonNull
    private String descrizione;

    @NonNull
    private LocalDate dataScadenza;

    @NonNull
    private LocalTime oraScadenza;

    private byte[] immagine;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "fk_categoria_asta_nome")
    @NonNull
    private CategoriaAsta categoria;

    @OneToMany(mappedBy = "astaAssociata", cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Notifica> notificheAssociate = new HashSet<>();

    // AllArgsConstructor
    protected Asta(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;

        this.categoria = categoria;
        categoria.addAstaAssegnata(this);
    }

    // Metodi per notificheAssociate
    public void addNotificaAssociata(Notifica notificaDaAggiungere) {
        this.notificheAssociate.add(notificaDaAggiungere);
    }

    public void removeNotificaAssociata(Notifica notificaDaRimuovere) {
        this.notificheAssociate.remove(notificaDaRimuovere);
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

        return "Asta(idAsta=" + this.getIdAsta() + ", nome=" + this.getNome() + ", descrizione=" + this.getDescrizione() + ", dataScadenza=" + this.getDataScadenza() + ", oraScadenza=" + this.getOraScadenza() + ", immagine=" + java.util.Arrays.toString(this.getImmagine()) + ", categoria=" + this.getCategoria().getNome() + ", notificheAssociate=" + listIdNotificheAssociate + ")";
    }
}
