package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na przechowywanie danych dotyczących oddziału szpitalnego.
 */
public class HospitalSection {

    /**
     * Pole przechowujące id oddziału.
     */
    private int id;

    /**
     * Pole przechowujące nazwę oddziału.
     */
    private String name;

    /**
     * Pole przechowujące id szpitala, w którym znajduje się oddział.
     */
    private int hospitalId;

    /**
     * Publiczny konstruktor klasy, który ustawia wszystkie pola klasy.
     *
     * @param id         - id oddziału
     * @param name       - nazwa oddziału
     * @param hospitalId - id szpitala, w którym znajduje sie oddział
     */
    public HospitalSection(int id, String name, int hospitalId) {
        this.id = id;
        this.name = name;
        this.hospitalId = hospitalId;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\", \"hospital_id\":" + hospitalId + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o oddziale
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}
