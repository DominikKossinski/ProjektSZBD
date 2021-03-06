package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Klasa umożliwiająca przechowywanie danych o pokoju.
 */
public class Room {

    /**
     * Pole przechowujące id pokoju.
     */
    private long id;

    /**
     * Pole przechowujące piętro.
     */
    private int floor;

    /**
     * Pole przechowujące liczbę miejsc.
     */
    private int numberOfPlaces;

    /**
     * Pole przechowujące id oddziału.
     */
    private long hospitalSectionId;

    /**
     * Pole przechowujące aktualną ilość zajętych miejsc.
     */
    private int actPlacedCount;

    /**
     * Publiczny konstruktor klasy ustawiający wszystkie pola.
     *
     * @param id                - id pokoju
     * @param floor             - piętro
     * @param numberOfPlaces    - liczba miejsc
     * @param hospitalSectionId - id oddziału
     */
    public Room(long id, int floor, int numberOfPlaces, long hospitalSectionId, int actPlacedCount) {
        this.id = id;
        this.floor = floor;
        this.numberOfPlaces = numberOfPlaces;
        this.hospitalSectionId = hospitalSectionId;
        this.actPlacedCount = actPlacedCount;
    }

    /**
     * Statyczna metoda służąca zwrócenia obiektu klasy Room na podstawie tekstu w formacie JSON.
     *
     * @param roomData - tekst w formacie json
     * @return (Element) - obiekt zawierający informacje o pokoju
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public static Room getInstance(String roomData) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject room = (JSONObject) parser.parse(roomData);
        return new Room((long) room.get("id"), Math.toIntExact((long) room.get("floor")),
                Math.toIntExact((long) room.get("number_of_places")),
                (long) room.get("hospital_section_id"),
                Math.toIntExact((long) room.get("act_placed_count")));
    }

    /**
     * Metoda klasy pozwalająca na sparsowanie obiektu do obiektu JSON.
     *
     * @return (JSONObject) - obiekt typu JSON zawierający dane o pokoju
     * @throws ParseException - błąd parsowania do formatu JSON
     */
    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }

    /**
     * Metoda klasy pozwalająca na przedstawienie obiektu zawierającego dane w formie tekstu w formacie JSON.
     *
     * @return (String) - tekst w formacie JSON, zawierający  dane o obiekcie
     */
    private String toJSONString() {
        return "{\"id\":" + id + ", \"floor\":" + floor + ", \"number_of_places\":" + numberOfPlaces + ", " +
                "\"hospital_section_id\":" + hospitalSectionId + ", \"act_placed_count\"" + actPlacedCount + "}";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHospitalSectionId() {
        return hospitalSectionId;
    }

    public int getActPlacedCount() {
        return actPlacedCount;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }
}
