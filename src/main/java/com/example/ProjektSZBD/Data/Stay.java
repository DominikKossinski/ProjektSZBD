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
    private long id;

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
    private long roomId;

    /**
     * Pole przechowujące id lekarza.
     */
    private long doctorId;

    /**
     * Pole przechowujące pesel pacjenta.
     */
    private long pesel;

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
    public Stay(long id, Date startDate, Date endDate, long roomId, long doctorId, long pesel) {
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

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Stay na podstawie tekstu w formacie JSON.
     *
     * @param stayData - tekst w formacie json
     * @return (Element) - obiekt zawierający informacje o pobycie
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Stay getInstance(String stayData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject stay = (JSONObject) parser.parse(stayData);
        if (stay.get("end_date") == null || String.valueOf(stay.get("end_date")).compareTo("") == 0) {
            return new Stay((long) stay.get("id"), Date.valueOf((String) stay.get("start_date")),
                    null, (long) stay.get("room_id"),
                    (long) stay.get("doctor_id"), (long) stay.get("pesel"));
        }
        return new Stay((long) stay.get("id"), Date.valueOf((String) stay.get("start_date")),
                Date.valueOf((String) stay.get("end_date")), (long) stay.get("room_id"),
                (long) stay.get("doctor_id"), (long) stay.get("pesel"));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPesel() {
        return pesel;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public long getRoomId() {
        return roomId;
    }
}
