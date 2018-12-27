package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na przechowywanie danych o lekarzu.
 */
public class Doctor {

    /**
     * Pole przechowujące id lekarza.
     */
    private int id;

    /**
     * Pole przechowujące imię lekarza.
     */
    private String firstName;

    /**
     * Pole przechowujące nazwisko lekarza.
     */
    private String lastName;

    /**
     * Pole przechowujące płacę lekarza.
     */
    private float salary;

    /**
     * Pole przechowujące id oddziału, na którym lekarz pracuje.
     */
    private int hospitalSectionId;

    /**
     * Pole przechowujące stanowisko lekarza.
     */
    private String position;

    /**
     * Pole przechowujące hasło lekarza.
     */
    private String password;

    /**
     * Publiczny konstruktor klasy ustawiający wszystkie informacje o lekarzu.
     *
     * @param id                - id lekarza
     * @param firstName         - imię lekarza
     * @param lastName          - nazwisko lekarza
     * @param salary            - płaca lekarza
     * @param hospitalSectionId - id oddziału
     * @param position          - stanowisko
     * @param password          - hasło
     */
    public Doctor(int id, String firstName, String lastName, float salary, int hospitalSectionId,
                  String position, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.hospitalSectionId = hospitalSectionId;
        this.position = position;
        this.password = password;
    }

    /**
     * Publiczny konstruktor klasy ustawiający podstawowe informacje o lekarzu.
     *
     * @param id                - id lekarza
     * @param firstName         - imię lekarza
     * @param lastName          - nazwisko lekarza
     * @param hospitalSectionId - id oddziału
     * @param position          - stanowisko
     */
    public Doctor(int id, String firstName, String lastName, int hospitalSectionId, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hospitalSectionId = hospitalSectionId;
        this.position = position;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego pełne dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\"" + id + ", \"first_name\":\"" + firstName + "\", " +
                "\"last_name\":\"" + lastName + "\", \"salary\":" + salary + ", " +
                "\"hospital_section_id\":" + hospitalSectionId + ", \"position\":\"" + position + "\", " +
                "\"password\":\"" + password + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego podstawowe dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toSimpleJSONString() {
        return "{\"id\"" + id + ", \"first_name\":\"" + firstName + "\", " +
                "\"last_name\":\"" + lastName + "\", " +
                "\"hospital_section_id\":" + hospitalSectionId + ", \"position\":\"" + position + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający pełne dane o lekarzu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający podstawowe dane o lekarzu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toSimpleJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toSimpleJSONString());
    }
}
