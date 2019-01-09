package com.example.ProjektSZBD.Data.Doctors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na reprezentowanie dyrektora szpitala
 */
public class Director extends Doctor {

    /**
     * Pole przechowujące id szpitala.
     */
    private long hospitalId;

    /**
     * Publiczny konstruktor klasy ustawiający podstawowe informacje o dyrektorze.
     *
     * @param hospitalId        - id szpitala
     * @param id                - id lekarza
     * @param firstName         - imię
     * @param lastName          - nazwisko
     * @param hospitalSectionId - id oddziału
     */
    public Director(long hospitalId, long id, String firstName, String lastName, long hospitalSectionId) {
        super(id, firstName, lastName, hospitalSectionId, "Dyrektor");
        this.hospitalId = hospitalId;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie danych w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON zawierający informacje o dyrektorze.
     */
    @Override
    protected String toJSONString() {
        return "{ \"hospital_id\":" + hospitalId + "\"doctor\":" + super.toSimpleJSONString() + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający pełne dane o dyrektorze.
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    @Override
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}
