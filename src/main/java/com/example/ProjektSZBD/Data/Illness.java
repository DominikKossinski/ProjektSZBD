package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa umożliwiająca przechowywanie danych o chorobie.
 */
public class Illness {

    /**
     * Pole przechowujące id choroby.
     */
    private long id;

    /**
     * Pole przechowujące nazwę choroby.
     */
    private String name;

    /**
     * Pole przechowujące opis choroby.
     */
    private String description;

    /**
     * Publiczny konstruktor klasy ustawiający wszystkie pola klasy.
     *
     * @param id          - id choroby
     * @param name        - nazwa
     * @param description - opis
     */
    public Illness(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\", \"description\":\"" + description + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o chorobie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Illness na podstawie tekstu w formacie JSON.
     *
     * @param illnessData - tekst w formacie json
     * @return (Element) - obiekt zawierający informacje o oddziale
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Illness getInstance(String illnessData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject illness = (JSONObject) parser.parse(illnessData);
        return new Illness((long) illness.get("id"), (String) illness.get("name"), (String) illness.get("description"));
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
}
