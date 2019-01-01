package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Room;

import java.util.List;

/**
 * Interfejs odpowiedzialny za wystawianie danych o pokojach.
 */
public interface RoomInterface {

    /**
     * Metoda zwracająca informacje o pokoju wyszukiwanym po id.
     *
     * @param id - id pokoju
     * @return (Room) - obiekt zawierający informacje o pokoju
     */
    Room getRoomById(long id);

    /**
     * Metoda zwracająca informacje o pokojach na danym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Room) - lista obiektów zawierających informacje o pokojach
     */
    List<Room> getRoomsByHospitalSectionId(long hospitalSectionId);

    /**
     * Metoda zwracająca informacje o wolnych pokojach na danym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Room) - lista obiektów zawierających informacje o wolnych pokojach
     */
    List<Room> getRoomsWithFreePlaces(long hospitalSectionId);
}
