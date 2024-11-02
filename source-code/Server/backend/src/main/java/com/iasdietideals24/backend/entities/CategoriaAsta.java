package com.iasdietideals24.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "categoria_asta")
public class CategoriaAsta {
    @Id
    @NonNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private Set<Asta> asteAssegnate = new HashSet<>();

    // AllArgsConstructor
    public CategoriaAsta(@NonNull String nome) {
        this.nome = nome;
    }

    // Metodi per asteAssegnate
    public void addAstaAssegnata(@NonNull Asta astaAssegnataDaAggiungere) {
        this.asteAssegnate.add(astaAssegnataDaAggiungere);
    }

    public void removeAstaAssegnata(@NonNull Asta astaAssegnataDaRimuovere) {
        this.asteAssegnate.remove(astaAssegnataDaRimuovere);
    }

    public String toString() {
        Iterator<Asta> itrAstaAssegnata = this.getAsteAssegnate().iterator();
        StringBuilder listIdAsteAssegnate = new StringBuilder();
        listIdAsteAssegnate.append("[");
        while (itrAstaAssegnata.hasNext()) {
            listIdAsteAssegnate.append(itrAstaAssegnata.next().getIdAsta()).append(", ");
        }
        listIdAsteAssegnate.append("]");

        return "CategoriaAsta(nome=" + this.getNome() + ", asteAssegnate=" + listIdAsteAssegnate + ")";
    }
}
