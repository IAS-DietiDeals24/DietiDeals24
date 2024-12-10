package com.iasdietideals24.backend.entities;

import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "asta")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Check(constraints = "data_scadenza > NOW()")
public abstract class Asta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asta_id_seq")
    @Column(name = "id_asta", nullable = false)
    private Long idAsta;

    @Enumerated(EnumType.STRING)
    @NonNull
    @Column(name = "stato", nullable = false)
    private StatoAsta stato = StatoAsta.ACTIVE;

    @NonNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NonNull
    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @NonNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @NonNull
    @Temporal(TemporalType.TIME)
    @Column(name = "ora_scadenza", nullable = false)
    private LocalTime oraScadenza;

    @Column(name = "immagine")
    private byte[] immagine;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "categoria_asta_nome", nullable = false)
    @NonNull
    private CategoriaAsta categoria;

    @OneToMany(mappedBy = "astaAssociata", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Notifica> notificheAssociate = new HashSet<>();

    // AllArgsConstructor
    protected Asta(@NonNull CategoriaAsta categoria, @NonNull String nome, @NonNull String descrizione, @NonNull LocalDate dataScadenza, @NonNull LocalTime oraScadenza, byte[] immagine, StatoAsta stato) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.oraScadenza = oraScadenza;
        this.immagine = immagine;
        this.stato = stato;

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

        return "Asta(idAsta=" + this.getIdAsta() + ", stato=" + this.getStato() + ", nome=" + this.getNome() + ", descrizione=" + this.getDescrizione() + ", dataScadenza=" + this.getDataScadenza() + ", oraScadenza=" + this.getOraScadenza() + ", immagine=[...]" + ", categoria=" + this.getCategoria().getNome() + ", notificheAssociate=" + listIdNotificheAssociate + ")";
    }
}
