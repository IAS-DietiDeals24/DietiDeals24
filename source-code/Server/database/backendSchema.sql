DROP SCHEMA IF EXISTS dd24 CASCADE;

CREATE SCHEMA dd24;

SET search_path TO dd24;

CREATE TABLE categoria_asta
(
    nome TEXT NOT NULL,
    CONSTRAINT pk_categoria_asta PRIMARY KEY (nome)
);

CREATE TABLE profilo
(
    nome_utente     TEXT NOT NULL,
    CONSTRAINT pk_profilo PRIMARY KEY (nome_utente),
    area_geografica TEXT,
    biografia       TEXT,
    cognome         TEXT NOT NULL,
    data_nascita    DATE NOT NULL,
    CONSTRAINT chk_data_nascita CHECK (data_nascita <= NOW()),
    genere          TEXT NOT NULL,
    nome            TEXT NOT NULL,
    link_personale  TEXT,
    link_facebook   TEXT,
    link_x          TEXT,
    link_instagram  TEXT,
    link_git_hub    TEXT,
    profile_picture BYTEA
);

CREATE TABLE compratore
(
    email               TEXT NOT NULL,
    CONSTRAINT pk_compratore PRIMARY KEY (email),
    password            TEXT NOT NULL,
    id_facebook         TEXT,
    id_git_hub          TEXT,
    id_google           TEXT,
    id_x                TEXT,
    profilo_nome_utente TEXT NOT NULL,
    CONSTRAINT fk_profilo_nome_utente FOREIGN KEY (profilo_nome_utente) REFERENCES profilo (nome_utente) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE FUNCTION cleanup_compratore()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM offerta_silenziosa
    WHERE compratore_email = OLD.email;
    DELETE
    FROM offerta_tempo_fisso
    WHERE compratore_email = OLD.email;
    DELETE
    FROM asta_inversa
    WHERE compratore_email = OLD.email;
    DELETE
    FROM destinatari
    WHERE account_email = OLD.email;
    DELETE
    FROM notifica
    WHERE account_email = OLD.email;
END
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_compratore
    BEFORE DELETE OR
        UPDATE OF email
    ON compratore
    FOR EACH ROW
EXECUTE FUNCTION cleanup_compratore();

CREATE TABLE venditore
(
    email               TEXT NOT NULL,
    CONSTRAINT pk_venditore PRIMARY KEY (email),
    password            TEXT NOT NULL,
    id_facebook         TEXT,
    id_git_hub          TEXT,
    id_google           TEXT,
    id_x                TEXT,
    profilo_nome_utente TEXT NOT NULL,
    CONSTRAINT fk_profilo_nome_utente FOREIGN KEY (profilo_nome_utente) REFERENCES profilo (nome_utente) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE FUNCTION cleanup_venditore()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM offerta_inversa
    WHERE venditore_email = OLD.email;
    DELETE
    FROM asta_tempo_fisso
    WHERE venditore_email = OLD.email;
    DELETE
    FROM asta_silenziosa
    WHERE venditore_email = OLD.email;
    DELETE
    FROM destinatari
    WHERE account_email = OLD.email;
    DELETE
    FROM notifica
    WHERE account_email = OLD.email;
END
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_venditore
    BEFORE DELETE OR
        UPDATE OF email
    ON venditore
    FOR EACH ROW
EXECUTE FUNCTION cleanup_venditore();

CREATE TABLE asta_inversa
(
    id_asta          BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_asta_inversa PRIMARY KEY (id_asta),
    data_scadenza    DATE          NOT NULL,
    CONSTRAINT chk_data_scadenza CHECK (data_scadenza > NOW()),
    descrizione      TEXT          NOT NULL,
    immagine         BYTEA,
    nome             TEXT          NOT NULL,
    ora_scadenza     TIME          NOT NULL,
    categoria_asta   TEXT          NOT NULL,
    CONSTRAINT fk_categoria_asta FOREIGN KEY (categoria_asta) REFERENCES categoria_asta (nome) ON UPDATE CASCADE ON DELETE CASCADE,
    compratore_email TEXT          NOT NULL,
    CONSTRAINT fk_compratore_email FOREIGN KEY (compratore_email) REFERENCES compratore (email) ON UPDATE CASCADE ON DELETE CASCADE,
    soglia_iniziale  DECIMAL(2, 2) NOT NULL,
    CONSTRAINT chk_soglia_iniziale CHECK (soglia_iniziale >= 0)
);

CREATE FUNCTION cleanup_asta_inversa()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM notifica
    WHERE asta_id_asta = OLD.id_asta;
END
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_asta_inversa
    BEFORE DELETE
    ON asta_inversa
    FOR EACH ROW
EXECUTE FUNCTION cleanup_asta_inversa();

CREATE TABLE asta_silenziosa
(
    id_asta         BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_asta_silenziosa PRIMARY KEY (id_asta),
    data_scadenza   DATE NOT NULL,
    CONSTRAINT chk_data_scadenza CHECK (data_scadenza > NOW()),
    descrizione     TEXT NOT NULL,
    immagine        BYTEA,
    nome            TEXT NOT NULL,
    ora_scadenza    TIME NOT NULL,
    categoria_asta  TEXT NOT NULL,
    CONSTRAINT fk_categoria_asta FOREIGN KEY (categoria_asta) REFERENCES categoria_asta (nome) ON UPDATE CASCADE ON DELETE CASCADE,
    venditore_email TEXT NOT NULL,
    CONSTRAINT fk_venditore_email FOREIGN KEY (venditore_email) REFERENCES venditore (email) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE FUNCTION cleanup_asta_silenziosa()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM notifica
    WHERE asta_id_asta = OLD.id_asta;
END
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_asta_silenziosa
    BEFORE DELETE
    ON asta_silenziosa
    FOR EACH ROW
EXECUTE FUNCTION cleanup_asta_silenziosa();

CREATE TABLE asta_tempo_fisso
(
    id_asta         BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_asta_tempo_fisso PRIMARY KEY (id_asta),
    data_scadenza   DATE          NOT NULL,
    CONSTRAINT chk_data_scadenza CHECK (data_scadenza > NOW()),
    descrizione     TEXT          NOT NULL,
    immagine        BYTEA,
    nome            TEXT          NOT NULL,
    ora_scadenza    TIME          NOT NULL,
    categoria_asta  TEXT          NOT NULL,
    CONSTRAINT fk_categoria_asta FOREIGN KEY (categoria_asta) REFERENCES categoria_asta (nome) ON UPDATE CASCADE ON DELETE CASCADE,
    venditore_email TEXT          NOT NULL,
    CONSTRAINT fk_venditore_email FOREIGN KEY (venditore_email) REFERENCES venditore (email) ON UPDATE CASCADE ON DELETE CASCADE,
    soglia_minima   DECIMAL(2, 2) NOT NULL,
    CONSTRAINT chk_soglia_minima CHECK (soglia_minima >= 0)
);

CREATE FUNCTION cleanup_asta_tempo_fisso()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM notifica
    WHERE asta_id_asta = OLD.id_asta;
END
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_asta_tempo_fisso
    BEFORE DELETE
    ON asta_tempo_fisso
    FOR EACH ROW
EXECUTE FUNCTION cleanup_asta_tempo_fisso();

CREATE TABLE notifica
(
    id_notifica   BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_notifica PRIMARY KEY (id_notifica),
    data_invio    DATE   NOT NULL,
    CONSTRAINT chk_data_invio CHECK (data_invio <= NOW()),
    messaggio     TEXT   NOT NULL,
    oraInvio      TIME   NOT NULL,
    asta_id_asta  BIGINT NOT NULL,
    account_email TEXT   NOT NULL
);

CREATE FUNCTION chk_asta_id_asta()
    RETURNS TRIGGER AS
$$
BEGIN
    IF
        NOT EXISTS (SELECT id_asta FROM asta_inversa WHERE id_asta = NEW.asta_id_asta) AND
        NOT EXISTS (SELECT id_asta FROM asta_silenziosa WHERE id_asta = NEW.asta_id_asta) AND
        NOT EXISTS (SELECT id_asta FROM asta_tempo_fisso WHERE id_asta = NEW.asta_id_asta) THEN
        RAISE EXCEPTION 'L''identificativo dell''asta del record inserito non referenzia un''asta esistente';
    ELSE
        RETURN NEW;
    END IF;
END;
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_asta_id_asta
    BEFORE INSERT OR
        UPDATE OF asta_id_asta
    ON notifica
    FOR EACH ROW
EXECUTE FUNCTION chk_asta_id_asta();

CREATE FUNCTION chk_account_email()
    RETURNS TRIGGER AS
$$
BEGIN
    IF
        NOT EXISTS (SELECT email FROM compratore WHERE email = NEW.account_email) AND
        NOT EXISTS (SELECT email FROM venditore WHERE email = NEW.account_email) THEN
        RAISE EXCEPTION 'L''email del record inserito non referenzia un account esistente';
    ELSE
        RETURN NEW;
    END IF;
END;
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER trg_account_email
    BEFORE INSERT OR
        UPDATE OF account_email
    ON notifica
    FOR EACH ROW
EXECUTE FUNCTION chk_account_email();

CREATE TABLE destinatari
(
    notifica_id_notifica BIGINT NOT NULL,
    CONSTRAINT fk_notifica_id_notifica FOREIGN KEY (notifica_id_notifica) REFERENCES notifica (id_notifica) ON UPDATE CASCADE ON DELETE CASCADE,
    account_email        TEXT   NOT NULL,
    CONSTRAINT pk_destinatari PRIMARY KEY (notifica_id_notifica, account_email)
);

CREATE TRIGGER trg_account_email
    BEFORE INSERT OR
        UPDATE OF account_email
    ON destinatari
    FOR EACH ROW
EXECUTE FUNCTION chk_account_email();

CREATE TABLE offerta_inversa
(
    id_offerta           BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_offerta_inversa PRIMARY KEY (id_offerta),
    data_invio           DATE          NOT NULL,
    CONSTRAINT chk_data_invio CHECK (data_invio <= NOW()),
    ora_invio            TIME          NOT NULL,
    valore               DECIMAL(2, 2) NOT NULL,
    CONSTRAINT chk_valore CHECK (valore > 0),
    venditore_email      TEXT          NOT NULL,
    CONSTRAINT fk_venditore_email FOREIGN KEY (venditore_email) REFERENCES venditore (email) ON UPDATE CASCADE ON DELETE CASCADE,
    asta_inversa_id_asta BIGINT        NOT NULL,
    CONSTRAINT fk_asta_inversa_id_asta FOREIGN KEY (asta_inversa_id_asta) REFERENCES asta_inversa (id_asta) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TYPE STATO AS ENUM (
    'ACCEPTED',
    'REJECTED',
    'PENDING'
    );

CREATE TABLE offerta_silenziosa
(
    id_offerta              BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_offerta_silenziosa PRIMARY KEY (id_offerta),
    data_invio              DATE          NOT NULL,
    CONSTRAINT chk_data_invio CHECK (data_invio <= NOW()),
    ora_invio               TIME          NOT NULL,
    valore                  DECIMAL(2, 2) NOT NULL,
    CONSTRAINT chk_valore CHECK (valore > 0),
    compratore_email        TEXT          NOT NULL,
    CONSTRAINT fk_compratore_email FOREIGN KEY (compratore_email) REFERENCES compratore (email) ON UPDATE CASCADE ON DELETE CASCADE,
    stato                   STATO         NOT NULL,
    asta_silenziosa_id_asta BIGINT        NOT NULL,
    CONSTRAINT fk_asta_silenziosa_id_asta FOREIGN KEY (asta_silenziosa_id_asta) REFERENCES asta_silenziosa (id_asta) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE offerta_tempo_fisso
(
    id_offerta               BIGINT GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_offerta_tempo_fisso PRIMARY KEY (id_offerta),
    data_invio               DATE          NOT NULL,
    CONSTRAINT chk_data_invio CHECK (data_invio <= NOW()),
    ora_invio                TIME          NOT NULL,
    valore                   DECIMAL(2, 2) NOT NULL,
    CONSTRAINT chk_valore CHECK (valore > 0),
    compratore_email         TEXT          NOT NULL,
    CONSTRAINT fk_compratore_email FOREIGN KEY (compratore_email) REFERENCES compratore (email) ON UPDATE CASCADE ON DELETE CASCADE,
    asta_tempo_fisso_id_asta BIGINT        NOT NULL,
    CONSTRAINT fk_asta_tempo_fisso_id_asta FOREIGN KEY (asta_tempo_fisso_id_asta) REFERENCES asta_tempo_fisso (id_asta) ON UPDATE CASCADE ON DELETE CASCADE
);