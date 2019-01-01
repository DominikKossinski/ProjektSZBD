package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na przechowywanie danych o szpitalu.
 */
public class Hospital {

    /**
     * Pole przechowujące id szpitala.
     */
    private long id;

    /**
     * Pole przechowujące nazwę szpitala.
     */
    private String name;

    /**
     * Pole przechowujące adres szpitala.
     */
    private String address;

    /**
     * Pole przechowujące nazwę miasta, w którym znajduje się szpital.
     */
    private String city;

    /**
     * Publiczny konstruktor klasy, któy ustawia wszystkie pola klasy.
     *
     * @param id      - id szpitala
     * @param name    - nazwa szpitala
     * @param address - adres szpitala
     * @param city    - nazwa miasta, w którym znajduje się szpital
     */
    public Hospital(long id, String name, String address, String city) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu w formie tekstu w formacie JSON.
     * * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\", \"address\":\"" +
                address + "\", \"city\":\"" + city + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o szpitalu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject hospital = (JSONObject) parser.parse(this.toJSONString());
        return hospital;
    }
}
