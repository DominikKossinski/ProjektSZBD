CREATE TABLE choroby
(
  id_choroby NUMBER       NOT NULL,
  nazwa      VARCHAR2(50) NOT NULL,
  opis       VARCHAR2(300),
  CONSTRAINT id_choroby_check CHECK ( id_choroby > 0 )
);

ALTER TABLE choroby
  ADD CONSTRAINT choroby_pk PRIMARY KEY (id_choroby);

CREATE TABLE elementy_wyposazenia
(
  id_elementu      NUMBER       NOT NULL,
  nazwa            VARCHAR2(50) NOT NULL,
  ilosc            NUMBER       NOT NULL,
  cena_jednostkowa DECIMAL(10, 2),
  id_oddzialu      NUMBER       NOT NULL,
  CONSTRAINT id_elementu_check CHECK ( id_elementu > 0 ),
  CONSTRAINT id_odd_el_check CHECK ( id_oddzialu > 0 )
);

ALTER TABLE elementy_wyposazenia
  ADD CONSTRAINT elementy_wyposazenia_pk PRIMARY KEY (id_elementu);

CREATE TABLE lekarze
(
  id_lekarza  NUMBER        NOT NULL,
  imie        VARCHAR2(50)  NOT NULL,
  nazwisko    VARCHAR2(100) NOT NULL,
  placa       NUMBER        NOT NULL,
  id_oddzialu NUMBER        NOT NULL,
  stanowisko  VARCHAR2(50)  NOT NULL,
  haslo       VARCHAR2(500) NOT NULL,
  CONSTRAINT id_lek_check CHECK ( id_lekarza < 10000000000 and id_lekarza > 0 ),
  CONSTRAINT id_odd_lek_check CHECK ( id_oddzialu > 0 )
);

/*ADD CONSTRAINT lek_placa_check CHECK ( placa >= (SELECT place.placa_min
                                               from place
                                               where place.stanowisko = stanowisko)
    and placa <= (SELECT place.placa_max
                  from place
                  where place.stanowisko = stanowisko));*/

ALTER TABLE lekarze
  ADD CONSTRAINT lekarze_pk PRIMARY KEY (id_lekarza);

CREATE TABLE oddzialy
(
  id_oddzialu NUMBER        NOT NULL,
  nazwa       VARCHAR2(200) NOT NULL,
  id_szpitala NUMBER        NOT NULL,
  CONSTRAINT id_oddzialu_check CHECK ( id_oddzialu > 0 ),
  CONSTRAINT id_szp_oddz_check CHECK ( id_szpitala > 0 )
);

ALTER TABLE oddzialy
  ADD CONSTRAINT oddzialy_pk PRIMARY KEY (id_oddzialu);

CREATE TABLE pacjenci
(
  pesel    NUMBER        NOT NULL,
  imie     VARCHAR2(50)  NOT NULL,
  nazwisko VARCHAR2(100) NOT NULL,
  haslo    VARCHAR2(500) NOT NULL,
  CONSTRAINT pesel_check CHECK ( pesel >= 10000000000 and pesel < 100000000000 )
);

ALTER TABLE pacjenci
  ADD CONSTRAINT pacjenci_pk PRIMARY KEY (pesel);

CREATE TABLE place
(
  stanowisko VARCHAR2(50) NOT NULL,
  placa_min  NUMBER       NOT NULL,
  placa_max  NUMBER       NOT NULL,
  CONSTRAINT placa_check CHECK ( placa_min <= placa_max )
);

ALTER TABLE place
  ADD CONSTRAINT place_pk PRIMARY KEY (stanowisko);

CREATE TABLE pobyty
(
  id_pobytu        NUMBER NOT NULL,
  termin_przyjecia DATE   NOT NULL,
  termin_wypisu    DATE,
  id_pokoju        NUMBER NOT NULL,
  id_lekarza       NUMBER NOT NULL,
  pesel            NUMBER NOT NULL,
  CONSTRAINT id_pok_pob_check CHECK ( id_pokoju > 0 ),
  CONSTRAINT id_lek_pob_check CHECK ( id_lekarza > 0 and id_lekarza < 10000000 ),
  CONSTRAINT pesel_pob_check CHECK ( pesel >= 10000000000 and pesel < 100000000000 ),
  CONSTRAINT id_pob_check CHECK ( id_pobytu > 0 ),
  CONSTRAINT date_pobyty_check check (termin_przyjecia < termin_wypisu)
);

ALTER TABLE pobyty
  ADD CONSTRAINT pobyty_pk PRIMARY KEY (id_pobytu);

CREATE TABLE pokoje
(
  id_pokoju             NUMBER           NOT NULL,
  pietro                NUMBER,
  liczba_miejsc         NUMBER           NOT NULL,
  ilosc_zajetych_miejsc NUMBER DEFAULT 0 NOT NULL,
  id_oddzialu           NUMBER           NOT NULL,
  CONSTRAINT id_pok_check CHECK ( id_pokoju > 0 ),
  CONSTRAINT pietro_check CHECK ( pietro >= 0),
  CONSTRAINT liczba_m_check CHECK ( liczba_miejsc > 0 ),
  CONSTRAINT ilosc_z_m_check CHECK ( ilosc_zajetych_miejsc >= 0 and liczba_miejsc >= ilosc_zajetych_miejsc ),
  CONSTRAINT id_oddz_pok_check CHECK ( id_oddzialu > 0 )
);



ALTER TABLE pokoje
  ADD CONSTRAINT pokoje_pk PRIMARY KEY (id_pokoju);

CREATE TABLE recepty
(
  id_recepty       NUMBER NOT NULL,
  data_wystawienia DATE   NOT NULL,
  dawkowanie       VARCHAR2(200),
  id_choroby       NUMBER NOT NULL,
  id_pobytu        NUMBER NOT NULL,
  CONSTRAINT id_rec_check CHECK ( id_recepty > 0 ),
  CONSTRAINT id_choroby_rec_check CHECK ( id_choroby > 0 ),
  CONSTRAINT id_pob_rec_check CHECK ( id_pobytu > 0 )
);

ALTER TABLE recepty
  ADD CONSTRAINT recepty_pk PRIMARY KEY (id_recepty);

CREATE TABLE szpitale
(
  id_szpitala    NUMBER        NOT NULL,
  nazwa_szpitala VARCHAR2(200) NOT NULL,
  adres          VARCHAR2(200) NOT NULL,
  miasto         VARCHAR2(100) NOT NULL,
  CONSTRAINT id_szpitala_check CHECK ( id_szpitala > 0 )
);

ALTER TABLE szpitale
  ADD CONSTRAINT szpitale_pk PRIMARY KEY (id_szpitala);

ALTER TABLE elementy_wyposazenia
  ADD CONSTRAINT elementy_wyp_oddzialy_fk FOREIGN KEY (id_oddzialu)
    REFERENCES oddzialy (id_oddzialu);

ALTER TABLE lekarze
  ADD CONSTRAINT lekarze_oddzialy_fk FOREIGN KEY (id_oddzialu)
    REFERENCES oddzialy (id_oddzialu);

ALTER TABLE lekarze
  ADD CONSTRAINT lekarze_place_fk FOREIGN KEY (stanowisko)
    REFERENCES place (stanowisko);

ALTER TABLE oddzialy
  ADD CONSTRAINT oddzialy_szpitale_fk FOREIGN KEY (id_szpitala)
    REFERENCES szpitale (id_szpitala);

ALTER TABLE pobyty
  ADD CONSTRAINT pobyty_lekarze_fk FOREIGN KEY (id_lekarza)
    REFERENCES lekarze (id_lekarza);

ALTER TABLE pobyty
  ADD CONSTRAINT pobyty_pacjenci_fk FOREIGN KEY (pesel)
    REFERENCES pacjenci (pesel);

ALTER TABLE pobyty
  ADD CONSTRAINT pobyty_pokoje_fk FOREIGN KEY (id_pokoju)
    REFERENCES pokoje (id_pokoju);

ALTER TABLE pokoje
  ADD CONSTRAINT pokoje_oddzialy_fk FOREIGN KEY (id_oddzialu)
    REFERENCES oddzialy (id_oddzialu);

ALTER TABLE recepty
  ADD CONSTRAINT recepty_choroby_fk FOREIGN KEY (id_choroby)
    REFERENCES choroby (id_choroby);

ALTER TABLE recepty
  ADD CONSTRAINT recepty_pobyty_fk FOREIGN KEY (id_pobytu)
    REFERENCES pobyty (id_pobytu);

/*Sekwencja dla szpitali*/
CREATE SEQUENCE szpitale_id_seq START WITH 3 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER szpitale_id_trg
  BEFORE
    INSERT
  ON szpitale
  FOR EACH ROW
  WHEN ( new.id_szpitala IS NULL )
BEGIN
  :new.id_szpitala := szpitale_id_seq.nextval;
END;

/*Sekwencja dla oddzialów*/
CREATE SEQUENCE oddzialy_id_seq START WITH 5 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER oddzialy_id_trg
  BEFORE
    INSERT
  ON oddzialy
  FOR EACH ROW
  WHEN ( new.id_oddzialu IS NULL )
BEGIN
  :new.id_oddzialu := oddzialy_id_seq.nextval;
END;

/**Sekwencja dla lekarzy*/
CREATE SEQUENCE lekarze_id_seq START WITH 10 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER lekarze_id_trg
  BEFORE
    INSERT
  ON lekarze
  FOR EACH ROW
  WHEN ( new.id_lekarza IS NULL )
BEGIN
  :new.id_lekarza := lekarze_id_seq.nextval;
END;

/** sekwencja dla elementów wyposażenia*/
CREATE SEQUENCE elementy_id_seq START WITH 5 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER elementy_id_trg
  BEFORE
    INSERT
  ON elementy_wyposazenia
  FOR EACH ROW
  WHEN ( new.id_elementu IS NULL )
BEGIN
  :new.id_elementu := elementy_id_seq.nextval;
END;

/**Sekwencja dla pokojów*/
CREATE SEQUENCE pokoje_id_seq START WITH 13 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER pokoje_id_trg
  BEFORE
    INSERT
  ON pokoje
  FOR EACH ROW
  WHEN ( new.id_pokoju IS NULL )
BEGIN
  :new.id_pokoju := pokoje_id_seq.nextval;
END;

/**Sekwencja dla pobytów */
CREATE SEQUENCE pobyty_id_seq START WITH 3 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER pobyty_id_trg
  BEFORE
    INSERT
  ON pobyty
  FOR EACH ROW
  WHEN ( new.id_pobytu IS NULL )
BEGIN
  :new.id_pobytu := pobyty_id_seq.nextval;
END;

/** Sekwencja dla chorób*/
CREATE SEQUENCE choroby_id_seq START WITH 22 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER choroby_id_trg
  BEFORE
    INSERT
  ON choroby
  FOR EACH ROW
  WHEN ( new.id_choroby IS NULL )
BEGIN
  :new.id_choroby := choroby_id_seq.nextval;
END;

/**Sekwencja dla recept*/
CREATE SEQUENCE recepty_id_seq START WITH 3 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER recepty_id_trg
  BEFORE
    INSERT
  ON recepty
  FOR EACH ROW
  WHEN ( new.id_recepty IS NULL )
BEGIN
  :new.id_recepty := recepty_id_seq.nextval;
END;


--Dodanie pierwszego szpitala
INSERT INTO SZPITALE(ID_SZPITALA, NAZWA_SZPITALA, ADRES, MIASTO)
VALUES (1, 'Szpital im. Zbigniewa Religi', 'ul. Szpitalna 28', 'Poznań');
--Dodanie drugiego szpitala
INSERT INTO SZPITALE(ID_SZPITALA, NAZWA_SZPITALA, ADRES, MIASTO)
VALUES (2, 'Szpital Wojskowy', 'ul. Polna 36', 'Poznań');


--Dodanie oddziałów pierwszego szpitala
INSERT INTO ODDZIALY(ID_ODDZIALU, NAZWA, ID_SZPITALA)
VALUES (1, 'Kardiologia', 1);
INSERT INTO ODDZIALY(ID_ODDZIALU, NAZWA, ID_SZPITALA)
VALUES (2, 'Chirurgia', 1);
--Dodanie oddziałów drugiego szpitala
INSERT INTO ODDZIALY(ID_ODDZIALU, NAZWA, ID_SZPITALA)
VALUES (3, 'Kardiologia', 2);
INSERT INTO ODDZIALY(ID_ODDZIALU, NAZWA, ID_SZPITALA)
VALUES (4, 'Chirurgia', 2);

--Dodawanie płac
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Dyrektor', 7500, 15000);
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Ordynator', 5000, 12000);
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Lekarz', 2500, 7500);
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Asystent', 3000, 5000);
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Rezydent', 1000, 4500);
INSERT INTO PLACE(STANOWISKO, PLACA_MIN, PLACA_MAX)
VALUES ('Praktykant', 500, 2500);

--Dodanie lekarzy z pierwszego oddziału
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (1, 'Jan', 'Kowalski', 1, 'Dyrektor', 'Janek123', 7500);
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (2, 'Stanisław', 'Nowak', 1, 'Ordynator', 'StasiekN', 5000);
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (3, 'Krystyna', 'Nowak', 1, 'Lekarz', 'haslo123', 2500);
--Dodanie lekarzy z drugiego oddziału
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (4, 'Bronisław', 'Skalpel', 2, 'Ordynator', 'BSLekarz', 5000);
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (5, 'Anna', 'Nowakowska', 2, 'Lekarz', 'NowaAnna', 2500);
--Dodanie lekarzy z trzeciego oddziału
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (6, 'Antoni', 'Stół', 3, 'Dyrektor', 'Antek123', 7500);
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (7, 'Anna', 'Stół', 3, 'Ordynator', 'Anna132', 5000);
--Dodanie lekarzy z czwartego oddziału
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (8, 'Wojciech', 'Operacja', 4, 'Dyrektor', 'op123', 7500);
INSERT INTO LEKARZE(ID_LEKARZA, IMIE, NAZWISKO, ID_ODDZIALU, STANOWISKO, HASLO, PLACA)
VALUES (9, 'Bolesław', 'Stół', 4, 'Ordynator', 'Bolek132', 5000);

--Dodawanie pacjentów
INSERT INTO PACJENCI(PESEL, IMIE, NAZWISKO, HASLO)
VALUES (10000000000, 'Dominik', 'Kossiński', 'Dom123');
INSERT INTO PACJENCI(PESEL, IMIE, NAZWISKO, HASLO)
VALUES (10000000001, 'Szymon', 'Gągorek', 'Szym123');
INSERT INTO PACJENCI(PESEL, IMIE, NAZWISKO, HASLO)
VALUES (10000000002, 'Antoni', 'Kowalski', 'Ant123');

--Dodawanie chorób
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (1, 'Cukrzyca typu 1');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (2, 'Cukrzyca typu 2');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (3, 'Martwica mięśnia sercowego');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (4, 'Udar');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (5, 'Miażdzyca');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (6, 'Zapalenie Płuc');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (7, 'Astma');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (8, 'Nowotwór skóry');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (9, 'Zakrzepica');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (10, 'Oparzenie stopnia 1');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (11, 'Oparzenie stopnia 2');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (12, 'Oparzenie stopnia 3');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (13, 'Oparzenie stopnia 4');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (14, 'Odmożenie stopnia 1');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (15, 'Odmożenie stopnia 2');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (16, 'Odmożenie stopnia 3');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (17, 'Odmożenie stopnia 4');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (18, 'Uszkodzenie tętnicy');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (19, 'Złamanie kości');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (20, 'Skręcenie stawu');
INSERT INTO CHOROBY(ID_CHOROBY, NAZWA)
VALUES (21, 'Zwichnięcie stawu');

--Dodanie pokojów oddziału pierwszego
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (1, 0, 4, 1);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (2, 0, 5, 1);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (3, 0, 5, 1);
--Dodanie pokojów oddziału drugiego
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (4, 1, 2, 2);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (5, 1, 4, 2);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (6, 1, 5, 2);
--Dodanie pokojów oddziału trzeciego
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (7, 0, 3, 3);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (8, 1, 3, 3);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (9, 1, 5, 3);
--Dodawanie pokojów oddziału czwartego
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (10, 0, 2, 4);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (11, 0, 5, 4);
INSERT INTO POKOJE(ID_POKOJU, PIETRO, LICZBA_MIEJSC, ID_ODDZIALU)
VALUES (12, 0, 3, 4);

--Dodanie pobytu
INSERT INTO POBYTY(TERMIN_PRZYJECIA, TERMIN_WYPISU, ID_POKOJU, ID_LEKARZA, PESEL, ID_POBYTU)
VALUES (DATE '2018-12-01', DATE '2018-12-10', 1, 1, 10000000000, 1);
INSERT INTO POBYTY(TERMIN_PRZYJECIA, TERMIN_WYPISU, ID_POKOJU, ID_LEKARZA, PESEL, ID_POBYTU)
VALUES (DATE '2018-12-01', DATE '2018-12-10', 1, 2, 10000000001, 2);

--Dodanie recept
INSERT INTO RECEPTY(ID_RECEPTY, DATA_WYSTAWIENIA, DAWKOWANIE, ID_CHOROBY, ID_POBYTU)
VALUES (1, DATE '2018-12-01', 'APAP dwa razy dziennie', 1, 1);
INSERT INTO RECEPTY(ID_RECEPTY, DATA_WYSTAWIENIA, DAWKOWANIE, ID_CHOROBY, ID_POBYTU)
VALUES (2, DATE '2018-12-01', 'Aspiryna dwa razy dziennie po obiedzie', 2, 2);

--Dodawanie elementów wyposażenia
INSERT INTO ELEMENTY_WYPOSAZENIA(ID_ELEMENTU, NAZWA, ILOSC, CENA_JEDNOSTKOWA, ID_ODDZIALU)
VALUES (1, 'SKALPEL', 50, 25.50, 1);
INSERT INTO ELEMENTY_WYPOSAZENIA(ID_ELEMENTU, NAZWA, ILOSC, CENA_JEDNOSTKOWA, ID_ODDZIALU)
VALUES (2, 'SKALPEL', 31, 25.50, 2);
INSERT INTO ELEMENTY_WYPOSAZENIA(ID_ELEMENTU, NAZWA, ILOSC, CENA_JEDNOSTKOWA, ID_ODDZIALU)
VALUES (3, 'SKALPEL', 25, 25.50, 3);
INSERT INTO ELEMENTY_WYPOSAZENIA(ID_ELEMENTU, NAZWA, ILOSC, CENA_JEDNOSTKOWA, ID_ODDZIALU)
VALUES (4, 'SKALPEL', 39, 25.50, 4);
