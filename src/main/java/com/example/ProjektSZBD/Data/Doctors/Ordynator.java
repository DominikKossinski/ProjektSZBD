package com.example.ProjektSZBD.Data.Doctors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa pozwalająca na przechowywanie danych o ordynatorach.
 */
public class Ordynator extends Doctor {

    /**
     * Pole przechowujące nazwę oddziału.
     */
    private String hospitalSectionName;

    /**
     * Pole przechowujące id szpitala.
     */
    private long hospitalId;


    /**
     * Publiczny konstruktor ustawiający podstawowe informacje o ordynatorach.
     *
     * @param hospitalSectionName - nazwa oddziału
     * @param hospitalId          - id szpitala
     * @param id                  - id lekarza
     * @param firstName           - imię
     * @param lastName            - nazwisko
     * @param hospitalSectionId   - id oddziału
     */
    public Ordynator(String hospitalSectionName, long hospitalId, long id, String firstName, String lastName, long hospitalSectionId) {
        super(id, firstName, lastName, hospitalSectionId, "Ordynator");
        this.hospitalSectionName = hospitalSectionName;
        this.hospitalId = hospitalId;
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego podstawowe dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    @Override
    protected String toJSONString() {
        return "{\"hospital_section_name\":\"" + hospitalSectionName + "\", " +
                "\"hospital_id\":" + hospitalId + ", \"doctor\":" + super.toSimpleJSONString() + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający pełne dane o lekarzu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    @Override
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}
