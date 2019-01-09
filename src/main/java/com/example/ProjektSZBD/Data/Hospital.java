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
     * Statyczna metoda służąca zwrócenia obiektu klasy Hospital na podstawie tekstu w formacie JSON.
     *
     * @param hospitalData - tekst w formacie json
     * @return (Hospital) - obiekt zawierający informacje o szpitalu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Hospital getInstance(String hospitalData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject hospital = (JSONObject) parser.parse(hospitalData);
        return new Hospital((long) hospital.get("id"),
                (String) hospital.get("name"), (String) hospital.get("address"), (String) hospital.get("city"));
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Hospital na podstawie obiektu JSON.
     *
     * @param hospital - obiekt JSON reprezentujący dane szpitala
     * @return (Hospital) - obiekt zawierający informacje o szpitalu
     */
    public static Hospital getInstance(JSONObject hospital) {
        return new Hospital((long) hospital.get("id"),
                (String) hospital.get("name"), (String) hospital.get("address"), (String) hospital.get("city"));
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

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }
}
