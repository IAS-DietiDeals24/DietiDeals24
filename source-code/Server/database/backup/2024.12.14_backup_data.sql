--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 16.3

-- Started on 2024-12-14 17:08:26 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3438 (class 0 OID 16412)
-- Dependencies: 219
-- Data for Name: categoria_asta; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.categoria_asta (nome) VALUES ('BOOKS');
INSERT INTO dd24.categoria_asta (nome) VALUES ('COMICS_AND_MANGAS');
INSERT INTO dd24.categoria_asta (nome) VALUES ('MUSIC');
INSERT INTO dd24.categoria_asta (nome) VALUES ('MOVIES_AND_TV_SHOWS');
INSERT INTO dd24.categoria_asta (nome) VALUES ('VIDEOGAMES_AND_CONSOLES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('ELECTRONICS');
INSERT INTO dd24.categoria_asta (nome) VALUES ('FOODS_AND_DRINKS');
INSERT INTO dd24.categoria_asta (nome) VALUES ('PETS_SUPPLIES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('BODYCARE_AND_BEAUTY');
INSERT INTO dd24.categoria_asta (nome) VALUES ('SPORTS_AND_HOBBIES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('CLOTHINGS_AND_WEARABLES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('HOME_AND_FURNITURES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('VEHICLES');
INSERT INTO dd24.categoria_asta (nome) VALUES ('OTHER');


--
-- TOC entry 3445 (class 0 OID 16451)
-- Dependencies: 226
-- Data for Name: profilo; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('Gio', '', '', 'Mel', '2002-12-31', '', 'Gio ', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('Tetrian', '', '', 'Cerrone ', '1997-06-29', '', 'Simone', '', '', '', '', '', '\x')
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('Giovann', 'montepulciano', 'ho 19 Anni e Nella Vita faccio il salumiere', 'Ino', '2005-12-15', 'uomo', 'Giovann', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('reb', 'Napoli ', '', 'cara', '2006-05-30', 'Non pervenuto', 'Rebecca ', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('vJoseph', 'Mozambico', 'Elicottero da battaglia ', 'Verdoliva', '2002-11-18', 'hasbullah', 'Giuseppe', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('Big', '', '', 'Mel', '2002-12-31', '', 'Gio', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('Zetzu', '', '', 'D''Alessio', '1997-10-15', '', 'Gigi', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('big', '', '', 'mel', '2002-12-31', '', 'gio', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('simons', 'La Valletta, Malta', 'Mi piace giocare a carte, nel tempo libero le riparo', 'Scisciola', '1989-07-13', 'Maschio', 'Simone', 'www.facebook.com', '', '', 'www.grandicarte.com', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('matthias.dagostino', 'Antille Olandesi', '', 'D''Agostino ', '2004-09-07', 'Vivace e giovanile, Matthias D''Agostino è un piccolo crossover. Rispetto a molti concorrenti diretti, è meno alto e più slanciato; ne deriva una linea quasi sportiva', 'Matthias ', '', '', '', 'https://youtu.be/m6oVZQKFsys?si=JG9PqQaHCHHWUiK3', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('cristina', 'napoli', 'giovane imprenditrice ', 'cristina', '2003-11-06', 'donna', 'cristina', 'noncelhopuntocom', 'nemmenoquesto', 'little.cris', 'Cristina ', 'link', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('armandolo', '', '', 'Piuma', '2024-12-06', '', 'Armando ', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('maistoi', '', '', 'maisto', '2024-12-13', '', 'imma', '', '', '', '', '', '\x');
INSERT INTO dd24.profilo (nome_utente, area_geografica, biografia, cognome, data_nascita, genere, nome, link_facebook, link_git_hub, link_instagram, link_personale, link_x, profile_picture) VALUES ('AndrexAce', 'Quarto, Napoli', 'Just myself', 'Lucchese', '2002-08-01', 'Uomo', 'Andrea', '', '', '', '', '', '\x');


--
-- TOC entry 3439 (class 0 OID 16417)
-- Dependencies: 220
-- Data for Name: compratore; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (1, 'simone.scisciola03@gmail.com', 'simone.scisciola03@gmail.com', '', '', '', '', 'simons');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (3, 'andrylook14@gmail.com', 'andrylook14@gmail.com', '', '', '', '', 'AndrexAce');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (52, 'melisgiosue9@gmail.com', 'melisgiosue9@gmail.com', '', '', '', '', 'Gio');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (59, 'tetrian@gmail.com', 'tetrian@gmail.com', '', '', '', '', 'Tetrian');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (60, 'giovannino5643688@gmail.com', 'giovannino5643688@gmail.com', '', '', '', '', 'Giovann');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (64, 'rebycaraviello@gmail.com', 'rebycaraviello@gmail.com', '', '', '', '', 'reb');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (102, 'giuseppeverdoliva2002@gmail.com', 'giuseppeverdoliva2002@gmail.com', '', '', '', '', 'vJoseph');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (104, 'matthias.dagostino@gmail.com', 'matthias.dagostino@gmail.com', '', '', '', '', 'matthias.dagostino');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (106, 'melisgiosue06@gmail.com', 'melisgiosue06@gmail.com', '', '', '', '', 'Big');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (108, 'asta@einrot.com', 'asta@einrot.com', '', '', '', '', 'Zetzu');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (110, 'melisgiosue01@gmail.com', 'melisgiosue01@gmail.com', '', '', '', '', 'big');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (152, 'cristina@gmail.com', 'cristina@gmail.com', '', '', '', '', 'cristina');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (154, 'armando@gmail.com', 'armando@gmail.com', '', '', '', '', 'armandolo');
INSERT INTO dd24.compratore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (159, 'maistoi76@gmail.com', 'maistoi76@gmail.com', '', '', '', '', 'maistoi');


--
-- TOC entry 3435 (class 0 OID 16386)
-- Dependencies: 216
-- Data for Name: asta_inversa; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (155, '2024-12-18', 'Carta rara giapponese ', '\x', 'Carta Pokemon', '22:59:00', 'ACTIVE', 'OTHER', 108, 1.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (158, '2024-12-20', 'Antiscivolo: la suola in gomma offre una buona protezione antiscivolo per evitare lesioni ai piedi causate da oggetti appuntiti e pietre.', '\x', 'Scarpe da scoglio ', '23:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 106, 5.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (205, '2024-12-15', 'usato pochissime volte nuovissimo', '\x', 'sterilizzatore estetica', '11:00:00', 'ACTIVE', 'BODYCARE_AND_BEAUTY', 152, 25.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (206, '2024-12-14', 'sterilizzatore come nuovo', '\x', 'sterilizzatore', '22:00:00', 'ACTIVE', 'BODYCARE_AND_BEAUTY', 152, 30.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (152, '2024-12-11', 'Tutti hanno bisogno di un Jotaro', '\x', 'Jotaro', '22:59:00', 'CLOSED', 'PETS_SUPPLIES', 102, 10.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (1, '2024-12-11', 'Una panchina', '\x', 'Panchina', '23:00:00', 'CLOSED', 'HOME_AND_FURNITURES', 3, 13.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (108, '2024-12-12', 'é buono', '\x', 'vino di montepulciano', '00:00:00', 'CLOSED', 'FOODS_AND_DRINKS', 60, 20.00);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (111, '2024-12-12', 'è una pizza', '\x', 'pizza', '19:00:00', 'CLOSED', 'OTHER', 64, 10866.64);
INSERT INTO dd24.asta_inversa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, compratore_id_account, soglia_iniziale) VALUES (107, '2024-12-13', 'Scarpe ', '\x', 'Scarpe da Scoglio Uomo Donna', '11:00:00', 'CLOSED', 'CLOTHINGS_AND_WEARABLES', 52, 5.00);


--
-- TOC entry 3446 (class 0 OID 16458)
-- Dependencies: 227
-- Data for Name: venditore; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (2, 'simone.scisciola03@gmail.com', 'simone.scisciola03@gmail.com', '', '', '', '', 'simons');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (4, 'andrylook14@gmail.com', 'andrylook14@gmail.com', '', '', '', '', 'AndrexAce');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (53, 'melisgiosue9@gmail.com', 'melisgiosue9@gmail.com', '', '', '', '', 'Gio');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (61, 'giovannino5643688@gmail.com', 'giovannino5643688@gmail.com', '', '', '', '', 'Giovann');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (65, 'rebycaraviello@gmail.com', 'rebycaraviello@gmail.com', '', '', '', '', 'reb');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (103, 'giuseppeverdoliva2002@gmail.com', 'giuseppeverdoliva2002@gmail.com', '', '', '', '', 'vJoseph');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (105, 'matthias.dagostino@gmail.com', 'matthias.dagostino@gmail.com', '', '', '', '', 'matthias.dagostino');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (107, 'melisgiosue06@gmail.com', 'melisgiosue06@gmail.com', '', '', '', '', 'Big');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (109, 'asta@einrot.com', 'asta@einrot.com', '', '', '', '', 'Zetzu');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (111, 'melisgiosue01@gmail.com', 'melisgiosue01@gmail.com', '', '', '', '', 'big');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (153, 'cristina@gmail.com', 'cristina@gmail.com', '', '', '', '', 'cristina');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (155, 'armando@gmail.com', 'armando@gmail.com', '', '', '', '', 'armandolo');
INSERT INTO dd24.venditore (id_account, email, password, id_facebook, id_git_hub, id_google, id_x, profilo_nome_utente) VALUES (160, 'maistoi76@gmail.com', 'maistoi76@gmail.com', '', '', '', '', 'maistoi');


--
-- TOC entry 3436 (class 0 OID 16395)
-- Dependencies: 217
-- Data for Name: asta_silenziosa; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (102, '2024-12-11', 'Tuta da sub in ottime condizioni, perfetta per visitare i fondali oceanici ma anche per tuffarsi nella gelide acque del fiume Calore.', '\x', 'Tuta da sub in ottime condizioni', '22:59:00', 'CLOSED', 'SPORTS_AND_HOBBIES', 2);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (204, '2024-12-12', 'è la cosa più preziosa che ho', '\x', 'bicchiere Spiderman e Iron man', '11:00:00', 'CLOSED', 'FOODS_AND_DRINKS', 105);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (202, '2024-12-13', 'Ottima cabina telefonica, fatta in UK. Usata da Willy per chiamare sua madre nel Galles.', '\x', 'Cabina telefonica made in UK', '01:00:00', 'CLOSED', 'HOME_AND_FURNITURES', 2);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (210, '2024-12-20', 'bello', '\x', 'abito ', '23:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 160);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (211, '2024-12-20', 'bello', '\x', 'abito ', '23:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 160);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (212, '2024-12-20', 'bello', '\x', 'abito ', '23:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 160);
INSERT INTO dd24.asta_silenziosa (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account) VALUES (213, '2024-12-20', 'bello', '\x', 'abito ', '23:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 160);


--
-- TOC entry 3437 (class 0 OID 16403)
-- Dependencies: 218
-- Data for Name: asta_tempo_fisso; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (153, '2024-12-28', 'Pallone ufficiale FIGC', '\x', 'Pallone originale FIGC', '12:00:00', 'ACTIVE', 'SPORTS_AND_HOBBIES', 2, 150.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (154, '2024-12-20', 'Un libro con una descrizione lunghissima così che si possa sapere se è possibile rompere l''applicazione', '\x', 'Libro', '03:00:00', 'ACTIVE', 'BOOKS', 2, 33.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (203, '2024-12-19', 'Bellissima moneta europea proveniente da Malta, raffigurante lo stemma dei cavalieri maltesi.', '\x', '€1 maltese', '07:00:00', 'ACTIVE', 'OTHER', 2, 1.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (207, '2024-12-26', 'Viaggio a Venezia con hotel 5 stelle, pensione completa. Desidero che i nostri ospiti abbiano la migliore accogliente possibile.', '\x', 'Viaggio a Venezia', '14:00:00', 'ACTIVE', 'OTHER', 155, 800.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (208, '2024-12-26', 'Viaggio a Venezia con hotel 5 stelle, pensione completa. Desidero che i nostri ospiti abbiano la migliore accogliente possibile.', '\x', 'Viaggio a Venezia', '14:00:00', 'ACTIVE', 'OTHER', 155, 800.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (209, '2024-12-21', 'maglione caldo ', '\x', 'maglione ', '21:00:00', 'ACTIVE', 'CLOTHINGS_AND_WEARABLES', 160, 700.00);
INSERT INTO dd24.asta_tempo_fisso (id_asta, data_scadenza, descrizione, immagine, nome, ora_scadenza, stato, categoria_asta_nome, venditore_id_account, soglia_minima) VALUES (52, '2024-12-13', 'Ottime carte Monarchs, perfette per giocare a Kaboo in un mercato alimentare a La Valletta. Una carta è stata restaurata.', '\x', 'Carte Monarchs', '14:00:00', 'CLOSED', 'OTHER', 2, 10.00);


--
-- TOC entry 3441 (class 0 OID 16429)
-- Dependencies: 222
-- Data for Name: notifica; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (52, '2024-12-10', 'Hai ricevuto una nuova offerta per la tua asta', '21:13:23.364', 52, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (53, '2024-12-10', 'Hai ricevuto una nuova offerta per la tua asta', '21:16:07.663', 52, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (102, '2024-12-10', 'Hai ricevuto una nuova offerta per la tua asta', '21:35:26.768', 52, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (103, '2024-12-10', 'Hai ricevuto una nuova offerta per la tua asta', '21:35:52.213', 52, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (104, '2024-12-10', 'Hai ricevuto una nuova offerta per la tua asta', '22:39:29.833', 1, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (105, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '08:54:16.811', 102, 52);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (106, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '09:14:42.838', 102, 59);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (107, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '10:38:07.354', 102, 60);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (108, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '11:32:46.483', 102, 64);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (152, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:00:59.68', 102, 102);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (153, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:01:30.559', 102, 102);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (154, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:04:29.267', 52, 102);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (155, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:17:24.182', 152, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (156, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:17:37.665', 111, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (157, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:41:16.014', 1, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (158, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '13:41:30.416', 107, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (159, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '17:45:06.774', 111, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (160, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '17:59:25.897', 102, 108);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (161, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '18:59:50.822', 102, 106);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (162, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '19:53:17.373', 1, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (202, '2024-12-11', 'La tua offerta silenziosa è stata rifiutata', '21:15:46.377', 102, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (203, '2024-12-11', 'La tua offerta silenziosa è stata rifiutata', '21:16:07.595', 102, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (204, '2024-12-11', 'La tua offerta silenziosa è stata rifiutata', '21:20:54.647', 102, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (205, '2024-12-11', 'La tua offerta silenziosa è stata accettata', '21:20:54.835', 102, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (206, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '21:51:22.243', 155, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (207, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '22:32:43.696', 202, 152);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (208, '2024-12-11', 'Hai ricevuto una nuova offerta per la tua asta', '22:33:08.985', 204, 152);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (209, '2024-12-11', 'Hai vinto l''asta', '22:59:26.507', 152, 102);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (210, '2024-12-11', 'La tua asta è scaduta', '22:59:26.647', 152, 102);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (211, '2024-12-11', 'Hai vinto l''asta', '23:00:26.699', 1, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (212, '2024-12-11', 'Hai perso l''asta', '23:00:26.841', 1, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (213, '2024-12-11', 'La tua asta è scaduta', '23:00:26.842', 1, 3);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (214, '2024-12-12', 'La tua asta è scaduta', '00:00:27.18', 108, 60);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (215, '2024-12-12', 'Hai ricevuto una nuova offerta per la tua asta', '07:47:31.181', 206, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (216, '2024-12-12', 'La tua offerta silenziosa è stata rifiutata', '11:00:00.829', 204, 105);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (217, '2024-12-12', 'La tua asta è scaduta', '11:00:00.938', 204, 105);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (218, '2024-12-12', 'Hai vinto l''asta', '19:00:03.651', 111, 64);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (219, '2024-12-12', 'Hai perso l''asta', '19:00:04.555', 111, 64);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (220, '2024-12-12', 'La tua asta è scaduta', '19:00:04.556', 111, 64);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (221, '2024-12-13', 'La tua offerta silenziosa è stata rifiutata', '01:00:07.898', 202, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (222, '2024-12-13', 'La tua asta è scaduta', '01:00:08.161', 202, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (223, '2024-12-13', 'Hai vinto l''asta', '11:00:11.949', 107, 52);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (224, '2024-12-13', 'La tua asta è scaduta', '11:00:12.022', 107, 52);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (225, '2024-12-13', 'Hai ricevuto una nuova offerta per la tua asta', '11:06:33.089', 206, 160);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (226, '2024-12-13', 'Hai vinto l''asta', '14:00:12.919', 52, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (227, '2024-12-13', 'Hai perso l''asta', '14:00:13.127', 52, 2);
INSERT INTO dd24.notifica (id_notifica, data_invio, messaggio, ora_invio, asta_id_asta, account_id_account) VALUES (228, '2024-12-13', 'La tua asta è scaduta', '14:00:13.136', 52, 2);


--
-- TOC entry 3440 (class 0 OID 16424)
-- Dependencies: 221
-- Data for Name: destinatari; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (52, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (53, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (102, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (103, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (104, 3);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (105, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (106, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (107, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (108, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (152, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (153, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (154, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (155, 102);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (156, 64);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (157, 3);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (158, 52);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (159, 64);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (160, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (161, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (162, 3);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (202, 52);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (203, 106);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (204, 102);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (204, 60);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (204, 108);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (204, 59);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (204, 64);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (205, 102);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (206, 108);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (207, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (208, 105);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (209, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (210, 102);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (211, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (212, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (213, 3);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (214, 60);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (215, 152);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (216, 152);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (217, 105);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (218, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (219, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (220, 64);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (221, 152);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (222, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (223, 2);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (224, 52);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (225, 152);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (226, 102);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (227, 3);
INSERT INTO dd24.destinatari (notifica_id_notifica, account_id_account) VALUES (228, 2);


--
-- TOC entry 3442 (class 0 OID 16435)
-- Dependencies: 223
-- Data for Name: offerta_inversa; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (104, '2024-12-10', '21:39:30', 10.00, 2, 1);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (155, '2024-12-11', '12:17:25', 8.00, 2, 152);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (156, '2024-12-11', '12:17:38', 10.00, 2, 111);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (157, '2024-12-11', '12:41:16', 8.00, 2, 1);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (158, '2024-12-11', '12:41:31', 4.90, 2, 107);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (159, '2024-12-11', '16:45:07', 9.00, 2, 111);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (162, '2024-12-11', '18:53:18', 7.90, 2, 1);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (202, '2024-12-11', '20:51:23', 0.90, 2, 155);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (205, '2024-12-12', '06:47:32', 18.00, 2, 206);
INSERT INTO dd24.offerta_inversa (id_offerta, data_invio, ora_invio, valore, venditore_id_account, asta_inversa_id_asta) VALUES (206, '2024-12-13', '10:06:33', 1.00, 160, 206);


--
-- TOC entry 3443 (class 0 OID 16440)
-- Dependencies: 224
-- Data for Name: offerta_silenziosa; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (105, '2024-12-11', '07:54:16', 1.00, 52, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (161, '2024-12-11', '17:59:50', 2.00, 106, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (153, '2024-12-11', '12:01:30', 1500.00, 102, 'ACCEPTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (106, '2024-12-11', '08:14:42', 42.00, 59, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (107, '2024-12-11', '10:38:06', 1.00, 60, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (108, '2024-12-11', '10:32:46', 2.00, 64, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (152, '2024-12-11', '12:00:59', 750.00, 102, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (160, '2024-12-11', '16:59:25', 2.00, 108, 'REJECTED', 102);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (204, '2024-12-11', '21:33:08', 2.00, 152, 'REJECTED', 204);
INSERT INTO dd24.offerta_silenziosa (id_offerta, data_invio, ora_invio, valore, compratore_id_account, stato, asta_silenziosa_id_asta) VALUES (203, '2024-12-11', '21:32:43', 3.00, 152, 'REJECTED', 202);


--
-- TOC entry 3444 (class 0 OID 16446)
-- Dependencies: 225
-- Data for Name: offerta_tempo_fisso; Type: TABLE DATA; Schema: dd24; Owner: -
--

INSERT INTO dd24.offerta_tempo_fisso (id_offerta, data_invio, ora_invio, valore, compratore_id_account, asta_tempo_fisso_id_asta) VALUES (52, '2024-12-10', '21:13:23.289', 11.00, 3, 52);
INSERT INTO dd24.offerta_tempo_fisso (id_offerta, data_invio, ora_invio, valore, compratore_id_account, asta_tempo_fisso_id_asta) VALUES (53, '2024-12-10', '21:16:07.622', 12.00, 3, 52);
INSERT INTO dd24.offerta_tempo_fisso (id_offerta, data_invio, ora_invio, valore, compratore_id_account, asta_tempo_fisso_id_asta) VALUES (102, '2024-12-10', '20:35:26', 13.00, 3, 52);
INSERT INTO dd24.offerta_tempo_fisso (id_offerta, data_invio, ora_invio, valore, compratore_id_account, asta_tempo_fisso_id_asta) VALUES (103, '2024-12-10', '20:35:52', 14.00, 3, 52);
INSERT INTO dd24.offerta_tempo_fisso (id_offerta, data_invio, ora_invio, valore, compratore_id_account, asta_tempo_fisso_id_asta) VALUES (154, '2024-12-11', '12:04:29', 300.00, 102, 52);


--
-- TOC entry 3456 (class 0 OID 0)
-- Dependencies: 228
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: dd24; Owner: -
--

SELECT pg_catalog.setval('dd24.account_id_seq', 201, true);


--
-- TOC entry 3457 (class 0 OID 0)
-- Dependencies: 229
-- Name: asta_id_seq; Type: SEQUENCE SET; Schema: dd24; Owner: -
--

SELECT pg_catalog.setval('dd24.asta_id_seq', 251, true);


--
-- TOC entry 3458 (class 0 OID 0)
-- Dependencies: 230
-- Name: notifica_id_seq; Type: SEQUENCE SET; Schema: dd24; Owner: -
--

SELECT pg_catalog.setval('dd24.notifica_id_seq', 251, true);


--
-- TOC entry 3459 (class 0 OID 0)
-- Dependencies: 231
-- Name: offerta_id_seq; Type: SEQUENCE SET; Schema: dd24; Owner: -
--

SELECT pg_catalog.setval('dd24.offerta_id_seq', 251, true);


-- Completed on 2024-12-14 17:08:43 UTC

--
-- PostgreSQL database dump complete
--

