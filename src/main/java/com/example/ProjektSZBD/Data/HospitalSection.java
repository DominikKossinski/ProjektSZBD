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
    private long id;

    /**
     * Pole przechowujące nazwę oddziału.
     */
    private String name;

    /**
     * Pole przechowujące id szpitala, w którym znajduje się oddział.
     */
    private long hospitalId;

    /**
     * Publiczny konstruktor klasy, który ustawia wszystkie pola klasy.
     *
     * @param id         - id oddziału
     * @param name       - nazwa oddziału
     * @param hospitalId - id szpitala, w którym znajduje sie oddział
     */
    public HospitalSection(long id, String name, long hospitalId) {
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy HospitalSection na podstawie tekstu w formacie JSON.
     *
     * @param hospitalSectionData - tekst w formacie json
     * @return (HospitalSection) - obiekt zawierający informacje o oddziale
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static HospitalSection getInstance(String hospitalSectionData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject hospitalSection = (JSONObject) parser.parse(hospitalSectionData);
        return new HospitalSection((long) hospitalSection.get("id"), (String) hospitalSection.get("name"),
                (long) hospitalSection.get("hospital_id"));
    }

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy HospitalSection na podstawie obiektu JSON.
     *
     * @param hospitalSection - obiekt JSON reprezentujący dane oddzialu
     * @return (HospitalSection) - obiekt zawierający informacje o szpitalu
     */
    public static HospitalSection getInstance(JSONObject hospitalSection) {
        return new HospitalSection((long) hospitalSection.get("id"), (String) hospitalSection.get("name"),
                (long) hospitalSection.get("hospital_id"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getHospitalId() {
        return hospitalId;
    }
}
