package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Date;

/**
 * Klasa umożliwiająca przechowywanie danych o chorobie.
 */
public class Prescription {

    /**
     * Pole przechowujące id recepty.
     */
    private long id;

    /**
     * Pole przechowujące datę wystawienia recepty.
     */
    private Date date;

    /**
     * Pole przechowujące opis dawkowania.
     */
    private String dosage;

    /**
     * Pole przechowujące id choroby.
     */
    private long illnessId;

    /**
     * Pole przechowujące id pobytu.
     */
    private long stayId;

    /**
     * Publiczny konstruktor ustawiający wszystkie pola klasy.
     *
     * @param id        - id recepty
     * @param date      - data wystawienia
     * @param dosage    - opis dawkowania
     * @param illnessId - id choroby
     * @param stayId    - id pobytu
     */
    public Prescription(long id, Date date, String dosage, long illnessId, long stayId) {
        this.id = id;
        this.date = date;
        this.dosage = dosage;
        this.illnessId = illnessId;
        this.stayId = stayId;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"date\":\"" + date + "\", " +
                "\"dosage\":\"" + dosage + "\", \"illnessId\":" + illnessId + ", " +
                "\"stayId\":" + stayId + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o recepcie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}

