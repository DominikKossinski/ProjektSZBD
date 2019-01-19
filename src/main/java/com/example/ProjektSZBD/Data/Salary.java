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
    double minSalary;

    /**
     * Pole przechowujące maksymalną płacę.
     */
    double maxSalary;

    /**
     * Publiczny konstruktor ustawiający wszystkie pola klasy.
     *
     * @param position  - stanowisko
     * @param minSalary - płaca minimalna
     * @param maxSalary - płaca maksymalna
     */
    public Salary(String position, double minSalary, double maxSalary) {
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Salary na podstawie tekstu w formacie JSON.
     *
     * @param salaryData - tekst w formacie json
     * @return (Element) - obiekt zawierający informacje o płacy
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Salary getInstance(String salaryData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject salary = (JSONObject) parser.parse(salaryData);
        return new Salary((String) salary.get("position"), Double.valueOf(String.valueOf(salary.get("min_salary"))),
                Double.valueOf(String.valueOf(salary.get("max_salary"))));
    }

    public String getPosition() {
        return position;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getMinSalary() {
        return minSalary;
    }
}
