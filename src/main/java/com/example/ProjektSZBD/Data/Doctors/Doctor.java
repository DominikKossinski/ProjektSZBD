package com.example.ProjektSZBD.Data.Doctors;

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
    private long id;

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
    private double salary;

    /**
     * Pole przechowujące id oddziału, na którym lekarz pracuje.
     */
    private long hospitalSectionId;

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
    public Doctor(long id, String firstName, String lastName, double salary, long hospitalSectionId,
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
    public Doctor(long id, String firstName, String lastName, long hospitalSectionId, String position) {
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
    protected String toJSONString() {
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
    String toSimpleJSONString() {
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Doctor na podstawie tekstu w formacie JSON.
     *
     * @param doctorData - tekst w formacie json
     * @return (Doctor) - obiekt zawierający informacje o lekarzu
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Doctor getInstance(String doctorData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject doctor = (JSONObject) parser.parse(doctorData);
        return getInstance(doctor);
    }

    public static Doctor getInstance(JSONObject doctor) {
        return new Doctor((long) doctor.get("id"), (String) doctor.get("first_name"), (String) doctor.get("last_name"),
                Double.valueOf(String.valueOf(doctor.get("salary"))), (long) doctor.get("hospital_section_id"),
                (String) doctor.get("position"), (String) doctor.get("password"));
    }


    public double getSalary() {
        return salary;
    }

    public long getHospitalSectionId() {
        return hospitalSectionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPosition() {
        return position;
    }

}
