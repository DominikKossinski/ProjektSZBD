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
  CONSTRAINT id_odd_el_check CHECK ( id_oddzialu > 0 ),
  CONSTRAINT ilosc_el_check CHECK ( ilosc > 0),
  CONSTRAINT cena_el_check CHECK ( cena_jednostkowa > 0 or null )
);

ALTER TABLE elementy_wyposazenia
  ADD CONSTRAINT elementy_wyposazenia_pk PRIMARY KEY (id_elementu);

CREATE TABLE lekarze
(
  id_lekarza  NUMBER          NOT NULL,
  imie        VARCHAR2(50)    NOT NULL,
  nazwisko    VARCHAR2(100)   NOT NULL,
  placa       NUMERIC(100, 2) NOT NULL,
  id_oddzialu NUMBER          NOT NULL,
  stanowisko  VARCHAR2(50)    NOT NULL,
  haslo       VARCHAR2(500)   NOT NULL,
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

--Funkcja do dodawania lekarza
CREATE OR REPLACE FUNCTION insertDoctor(firstName IN VARCHAR,
                                        lastName IN VARCHAR,
                                        salary IN NUMERIC,
                                        hospitalSectionId IN NUMBER,
                                        position IN VARCHAR,
                                        password IN VARCHAR,
                                        doctorId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO LEKARZE(imie, nazwisko, placa, id_oddzialu, stanowisko, haslo)
  VALUES (firstName, lastName, salary, hospitalSectionId, position, password) returning ID_LEKARZA into doctorId;
  RETURN doctorId; --Id n owo utworzonego lekarza
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      doctorId := -3; --Naruszenie więzów spójności
    elsif (SQLCODE = -02290) THEN
      doctorId := -2; --Naruszenie więzów chceck
    else
      doctorId := -1; --Inny błąd
    end if;
    RETURN doctorId;
END;

CREATE OR REPLACE FUNCTION deleteDoctor(doctorId IN VARCHAR,
                                        rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM LEKARZE WHERE ID_LEKARZA = doctorId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION updateDoctor(doctorId IN NUMBER,
                                        firstName IN VARCHAR,
                                        lastName IN VARCHAR,
                                        salary IN NUMERIC,
                                        hospitalSectionId IN NUMBER,
                                        position IN VARCHAR,
                                        password IN VARCHAR,
                                        rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE LEKARZE
  SET imie        = firstName,
      nazwisko    = lastName,
      placa       = salary,
      id_oddzialu = hospitalSectionId,
      stanowisko  = position,
      haslo       = password
  WHERE ID_LEKARZA = doctorId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne upade
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION updateDoctorNoPassword(doctorId IN NUMBER,
                                                  firstName IN VARCHAR,
                                                  lastName IN VARCHAR,
                                                  salary IN NUMERIC,
                                                  hospitalSectionId IN NUMBER,
                                                  position IN VARCHAR,
                                                  rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE LEKARZE
  SET imie        = firstName,
      nazwisko    = lastName,
      placa       = salary,
      id_oddzialu = hospitalSectionId,
      stanowisko  = position
  WHERE ID_LEKARZA = doctorId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne upade
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



--Funkcja do wstawiania elementu wyposażenia
CREATE OR REPLACE FUNCTION insertElement(name IN VARCHAR,
                                         count IN NUMERIC,
                                         price IN NUMERIC,
                                         hospitalSectionId IN NUMBER,
                                         elementId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO ELEMENTY_WYPOSAZENIA(nazwa, ilosc, cena_jednostkowa, id_oddzialu)
  VALUES (name, count, price, hospitalSectionId) returning ID_ELEMENTU into elementId;
  RETURN elementId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      elementId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      elementId := -2; /*Naruszenie więzów chceck */
    else
      elementId := -1;
    end if;
    RETURN elementId;
END;

CREATE OR REPLACE FUNCTION updateElement(elementId IN NUMBER,
                                         name IN VARCHAR,
                                         count IN NUMERIC,
                                         price IN NUMERIC,
                                         hospitalSectionId IN NUMBER,
                                         rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE ELEMENTY_WYPOSAZENIA
  SET nazwa            = name,
      ilosc            = count,
      cena_jednostkowa = price,
      id_oddzialu      = hospitalSectionId
  WHERE ID_ELEMENTU = elementId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne upade
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



CREATE OR REPLACE FUNCTION insertHospital(name IN VARCHAR,
                                          address IN VARCHAR,
                                          city IN VARCHAR,
                                          sectionName IN VARCHAR,
                                          firstName IN VARCHAR,
                                          lastName IN VARCHAR,
                                          salary IN NUMERIC,
                                          password IN VARCHAR,
                                          hospitalId OUT NUMBER,
                                          hospitalSectionId OUT NUMBER,
                                          doctorId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO SZPITALE(NAZWA_SZPITALA, ADRES, MIASTO)
  VALUES (name, address, city) returning ID_SZPITALA into hospitalId;
  INSERT INTO ODDZIALY(NAZWA, ID_SZPITALA)
  VALUES (sectionName, hospitalId) returning ID_ODDZIALU into hospitalSectionId;
  INSERT INTO LEKARZE(IMIE, NAZWISKO, PLACA, ID_ODDZIALU, STANOWISKO, HASLO)
  VALUES (firstName, lastName, salary, hospitalSectionId, 'Dyrektor', password) returning ID_LEKARZA into doctorId;
  RETURN hospitalId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      hospitalId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      hospitalId := -2; /*Naruszenie więzów chceck */
    else
      hospitalId := SQLCODE;
    end if;
    RETURN hospitalId;
END;

CREATE OR REPLACE FUNCTION deleteHospital(hospitalId IN NUMBER,
                                          rowC OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM SZPITALE WHERE ID_SZPITALA = hospitalId;
  rowC := SQL%ROWCOUNT;
  if (rowC = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowC = 0) then
    return -4; --Brak id
  else
    rollback;
    return -3; --Nie poprawne usunięcie
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION insertHospitalSection(name IN VARCHAR,
                                                 hospitalId IN NUMERIC,
                                                 hospitalSectionId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO ODDZIALY(nazwa, id_szpitala) VALUES (name, hospitalId) returning ID_ODDZIALU into hospitalSectionId;
  RETURN hospitalSectionId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      hospitalSectionId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      hospitalSectionId := -2; /*Naruszenie więzów chceck */
    else
      hospitalSectionId := -1;
    end if;
    RETURN hospitalSectionId;
END;
CREATE OR REPLACE FUNCTION updateHospitalSection(hospitalSectionId IN NUMBER,
                                                 name IN VARCHAR,
                                                 hospitalId IN NUMERIC,
                                                 rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE ODDZIALY SET nazwa = name, id_szpitala = hospitalId WHERE ID_ODDZIALU = hospitalSectionId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION deleteHospitalSection(hospitalSectionId IN NUMBER,
                                                 rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM ODDZIALY WHERE ID_ODDZIALU = hospitalSectionId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION insertIllness(name IN VARCHAR,
                                         description IN VARCHAR,
                                         illnessId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO CHOROBY(NAZWA, OPIS) VALUES (name, description) RETURNING ID_CHOROBY INTO illnessId;
  RETURN illnessId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      illnessId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      illnessId := -2; /*Naruszenie więzów chceck */
    else
      illnessId := -1;
    end if;
    RETURN illnessId;
END;

CREATE OR REPLACE FUNCTION updateIllness(illnessId IN NUMBER,
                                         name IN VARCHAR,
                                         description IN VARCHAR,
                                         rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE CHOROBY SET NAZWA = name, OPIS = description WHERE ID_CHOROBY = illnessId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION deleteIllness(illnessId IN NUMBER,
                                         rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM CHOROBY WHERE ID_CHOROBY = illnessId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;

CREATE OR REPLACE FUNCTION insertPatient(peselV IN NUMBER,
                                         firstName IN VARCHAR,
                                         lastName IN VARCHAR,
                                         password IN VARCHAR)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO PACJENCI(pesel, imie, nazwisko, haslo) VALUES (peselV, firstName, lastName, password);
  RETURN 0;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      RETURN -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      RETURN -2; /*Naruszenie więzów chceck */
    else
      RETURN -1;
    end if;
END;

CREATE OR REPLACE FUNCTION updatePatient(peselV IN NUMBER,
                                         firstName IN VARCHAR,
                                         lastName IN VARCHAR,
                                         password IN VARCHAR,
                                         rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE PACJENCI SET IMIE = firstName, NAZWISKO = lastName, HASLO = password WHERE PESEL = peselV;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION deletePatient(peselV IN NUMBER,
                                         rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM PACJENCI WHERE PESEL = peselV;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



CREATE OR REPLACE FUNCTION insertPrescription(dateP IN date,
                                              dosage IN VARCHAR,
                                              illnessId IN NUMBER,
                                              stayId IN NUMBER,
                                              prescriptionId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO RECEPTY(DATA_WYSTAWIENIA, DAWKOWANIE, ID_CHOROBY, ID_POBYTU)
  VALUES (dateP, dosage, illnessId, stayId) returning ID_RECEPTY INTO prescriptionId;
  RETURN prescriptionId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      prescriptionId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      prescriptionId := -2; /*Naruszenie więzów chceck */
    else
      prescriptionId := -1;
    end if;
    RETURN prescriptionId;
END;


CREATE OR REPLACE FUNCTION updatePrescription(prescriptionId IN NUMBER,
                                              dateP IN date,
                                              dosage IN VARCHAR,
                                              illnessId IN NUMBER,
                                              stayId IN NUMBER,
                                              rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE RECEPTY
  SET DATA_WYSTAWIENIA = dateP,
      DAWKOWANIE       = dosage,
      ID_CHOROBY       = illnessId,
      ID_POBYTU        = stayId
  WHERE ID_RECEPTY = prescriptionId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION deletePrescription(prescriptionId IN NUMBER,
                                              rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM RECEPTY WHERE ID_RECEPTY = prescriptionId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



CREATE OR REPLACE FUNCTION insertRoom(floor IN NUMBER,
                                      numberOfPlaces IN NUMBER,
                                      hospitalSectionId IN NUMBER,
                                      actPlacedCount IN NUMBER,
                                      roomId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO POKOJE(PIETRO, LICZBA_MIEJSC, ID_ODDZIALU, ILOSC_ZAJETYCH_MIEJSC)
  VALUES (floor, numberOfPlaces, hospitalSectionId, actPlacedCount) RETURNING id_pokoju INTO roomId;
  RETURN roomId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      roomId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      roomId := -2; /*Naruszenie więzów chceck */
    else
      roomId := -1;
    end if;
    RETURN roomId;
END;


CREATE OR REPLACE FUNCTION updateRoom(roomId IN NUMBER,
                                      floor IN NUMBER,
                                      numberOfPlaces IN NUMBER,
                                      hospitalSectionId IN NUMBER,
                                      actPlacedCount IN NUMBER,
                                      rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE POKOJE
  SET PIETRO                = floor,
      LICZBA_MIEJSC         = numberOfPlaces,
      ID_ODDZIALU           = hospitalSectionId,
      ILOSC_ZAJETYCH_MIEJSC = actPlacedCount
  WHERE id_pokoju = roomId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION deleteRoom(roomId IN NUMBER,
                                      rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM POKOJE WHERE id_pokoju = roomId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



CREATE OR REPLACE FUNCTION insertSalary(position IN VARCHAR,
                                        minSalary IN NUMERIC,
                                        maxSalary IN NUMERIC)
  RETURN NUMBER
IS

BEGIN

  INSERT INTO PLACE(stanowisko, placa_min, placa_max) VALUES (position, minSalary, maxSalary);
  return 1;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      return -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      return -2; /*Naruszenie więzów chceck */
    else
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION updateSalary(position IN VARCHAR,
                                        minSalary IN NUMERIC,
                                        maxSalary IN NUMERIC,
                                        rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  UPDATE place SET placa_min = minSalary, placa_max = maxSalary where stanowisko = position;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION deleteSalary(position IN VARCHAR,
                                        rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN

  DELETE FROM PLACE WHERE stanowisko = position;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;



CREATE OR REPLACE FUNCTION insertStay(startDate IN DATE,
                                      endDate IN DATE,
                                      roomId IN NUMBER,
                                      doctorId IN NUMBER,
                                      peselV IN NUMBER,
                                      stayId OUT NUMBER)
  RETURN NUMBER
IS

BEGIN
  INSERT INTO POBYTY(TERMIN_PRZYJECIA, TERMIN_WYPISU, ID_POKOJU, ID_LEKARZA, PESEL)
  VALUES (startDate, endDate, roomId, doctorId, peselV) returning ID_POBYTU into stayId;
  update pokoje
  set ilosc_zajetych_miejsc = (select count(id_pobytu)
                               from pobyty
                               where pobyty.id_pokoju = pokoje.id_pokoju
                                 and termin_wypisu is null)
  where id_pokoju = roomId;
  return stayId;
  EXCEPTION
  WHEN OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      stayId := -3; /*Naruszenie więzów spójności*/
    elsif (SQLCODE = -02290) THEN
      stayId := -2; /*Naruszenie więzów chceck */
    else
      stayId := -1;
    end if;
    RETURN stayId;
END;


CREATE OR REPLACE FUNCTION updateStay(stayId IN NUMBER,
                                      startDate IN DATE,
                                      endDate IN DATE,
                                      roomId IN NUMBER,
                                      doctorId IN NUMBER,
                                      peselV IN NUMBER,
                                      rowCount OUT NUMBER)
  RETURN NUMBER
IS

BEGIN
  UPDATE POBYTY
  SET termin_przyjecia = startDate,
      termin_wypisu    = endDate,
      id_pokoju        = roomId,
      id_lekarza       = doctorId,
      pesel            = peselV
  where ID_POBYTU = stayId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    update pokoje
    set ilosc_zajetych_miejsc = (select count(id_pobytu)
                                 from pobyty
                                 where pobyty.id_pokoju = pokoje.id_pokoju
                                   and termin_wypisu is null)
    where id_pokoju = roomId;
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne update
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02291) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


CREATE OR REPLACE FUNCTION deleteStay(stayId IN NUMBER,
                                      rowCount OUT NUMBER)
  RETURN NUMBER
IS
  roomId number;
BEGIN
  SELECT id_pokoju into roomId from pobyty where id_pobytu = stayId;
  DELETE FROM POBYTY WHERE ID_POBYTU = stayId;
  rowCount := SQL%ROWCOUNT;
  if (rowCount = 1) then
    update pokoje
    set ilosc_zajetych_miejsc = (select count(id_pobytu)
                                 from pobyty
                                 where pobyty.id_pokoju = pokoje.id_pokoju
                                   and termin_wypisu is null)
    where id_pokoju = roomId;
    return 0; --Poprawne zakończenie
  elsif (rowCount = 0) then
    return -4; --Nie ma id
  else
    rollback;
    return -3; --Nie poprawne delete
  end if;

  EXCEPTION
  WHEN
  OTHERS
  THEN
    if (SQLCODE = -02292) THEN
      rollback;
      return -2; --Naruszenie więzów spójności
    else
      rollback;
      return -1;
    end if;
END;


