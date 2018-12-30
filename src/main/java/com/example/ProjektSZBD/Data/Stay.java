package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Date;

/**
 * Klasa umożliwiająca przechowywanie danych o pobycie.
 */
public class Stay {

    /**
     * Pole przechowujące id pobytu.
     */
    private int id;

    /**
     * Pole przechowujące datę początku pobytu.
     */
    private Date startDate;

    /**
     * Pole przechowujące datę końca pobytu.
     */
    private Date endDate;

    /**
     * Pole przechowujące id pokoju.
     */
    private int roomId;

    /**
     * Pole przechowujące id lekarza.
     */
    private int doctorId;

    /**
     * Pole przechowujące pesel pacjenta.
     */
    private int pesel;

    /**
     * Publiczny konstruktor ustawiający wszystkie pola klasy.
     *
     * @param id        - id pobytu
     * @param startDate - data początku pobytu
     * @param endDate   - data końca pobytu
     * @param roomId    - id pokoju
     * @param doctorId  - id lekarza
     * @param pesel     - pesel pacjenta
     */
    public Stay(int id, Date startDate, Date endDate, int roomId, int doctorId, int pesel) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
        this.doctorId = doctorId;
        this.pesel = pesel;
    }


    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"start_date\":\"" + startDate + "\", \"end_date\":\"" + endDate + "\", " +
                "\"room_id\":" + roomId + ", \"doctor_id\":" + doctorId + ", \"pesel\":" + pesel + "}";
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o pobycie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }

}
