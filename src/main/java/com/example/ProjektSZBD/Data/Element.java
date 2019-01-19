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
    private long id;

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
    private double price;

    /**
     * Pole przechowujące id oddziału.
     */
    private long hospitalSectionId;

    /**
     * Publiczny konstruktor klasy ustawiający wszystkie pola klasy.
     *
     * @param id                - id elementu
     * @param name              - nazwa
     * @param count             - ilość
     * @param price             - cena jednostkowa
     * @param hospitalSectionId - id oddziału
     */
    public Element(long id, String name, int count, double price, long hospitalSectionId) {
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Element na podstawie tekstu w formacie JSON.
     *
     * @param elementData - tekst w formacie json
     * @return (Element) - obiekt zawierający informacje o elemencie wyposażenia
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Element getInstance(String elementData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject element = (JSONObject) parser.parse(elementData);
        return new Element((long) element.get("id"), (String) element.get("name"),
                Integer.parseInt(String.valueOf(element.get("count"))),
                Double.valueOf(String.valueOf(element.get("price"))),
                (long) element.get("hospital_section_id"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public long getHospitalSectionId() {
        return hospitalSectionId;
    }
}
