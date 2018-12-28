package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa umożliwiająca przechowywanie danych o płacy.
 */
public class Salary {

    /**
     * Pole przechowujące nazwę stanowiska.
     */
    String position;

    /**
     * Pole przechowujące minimalną płacę.
     */
    float minSalary;

    /**
     * Pole przechowujące maksymalną płacę.
     */
    float maxSalary;

    /**
     * Publiczny konstruktor ustawiający wszystkie pola klasy.
     *
     * @param position  - stanowisko
     * @param minSalary - płaca minimalna
     * @param maxSalary - płaca maksymalna
     */
    public Salary(String position, float minSalary, float maxSalary) {
        this.position = position;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }


    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"position\":\"" + position + "\", \"min_salary\":" + minSalary + ", \"max_salary\":" + maxSalary + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o płacy
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}
