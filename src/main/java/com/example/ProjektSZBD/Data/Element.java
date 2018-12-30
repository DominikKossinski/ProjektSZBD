package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa umożliwiająca przechowywanie informacji na temat elementów wyposażenia.
 */
public class Element {

    /**
     * Pole przechowujące id.
     */
    private int id;

    /**
     * Pole Przechowujące nazwę.
     */
    private String name;

    /**
     * Pole przechowujące ilość.
     */
    private int count;

    /**
     * Pole przechowujące cenę.
     */
    private float price;

    /**
     * Pole przechowujące id oddziału.
     */
    private int hospitalSectionId;

    /**
     * Publiczny konstruktor klasy ustawiający wszystkie pola klasy.
     *
     * @param id                - id elementu
     * @param name              - nazwa
     * @param count             - ilość
     * @param price             - cena jednostkowa
     * @param hospitalSectionId - id oddziału
     */
    public Element(int id, String name, int count, float price, int hospitalSectionId) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
        this.hospitalSectionId = hospitalSectionId;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"name\":\"" + name + "\", \"count\":" + count + ", " +
                "\"price\":" + price + ", \"hospital_section_id\":" + hospitalSectionId + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o elemencie wyposażenia
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}