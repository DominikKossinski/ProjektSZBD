package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na przechowywanie danych o pacjencie.
 */
public class Patient {

    /**
     * Pole przechowujące pesel.
     */
    private int pesel;

    /**
     * Pole przechowujące imię pacjenta.
     */
    private String firstName;

    /**
     * Pole przechowujące nazwisko pacjenta.
     */
    private String lastName;

    /**
     * Pole przechowujące hasło pacjenta.
     */
    private String password;

    /**
     * Publiczny konstruktor klasy ustawiający pełne dane o pacjencie.
     *
     * @param pesel     - pesel
     * @param firstName - imię
     * @param lastName  - nazwisko
     * @param password  - hasło
     */
    public Patient(int pesel, String firstName, String lastName, String password) {
        this.pesel = pesel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    /**
     * Publiczny konstruktor klasy ustawiający podstawowe dane o pacjencie.
     *
     * @param pesel     - pesel
     * @param firstName - imię
     * @param lastName  - nazwisko
     */
    public Patient(int pesel, String firstName, String lastName) {
        this.pesel = pesel;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego pełne dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"pesel\":" + pesel + ", \"first_name\":\"" + firstName + "\", " +
                "\"last_name\":\"" + lastName + "\", \"password\":\"" + password + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego podstawowe dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toSimpleJSONString() {
        return "{\"pesel\":" + pesel + ", \"first_name\":\"" + firstName + "\", " +
                "\"last_name\":\"" + lastName + "\"}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający pełne dane o pacjencie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający podstawowe dane o pacjencie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toSimpleJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toSimpleJSONString());
    }

}
