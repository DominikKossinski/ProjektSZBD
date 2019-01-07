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

    /**
     * Metoda do obsługi wstawiania pokoju.
     *
     * @param room - obiekt reprezentujący pokój
     * @return (long) - id pokoju w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    long insertRoom(Room room);

    /**
     * Metoda do obsługi aktualizowania danych pokoju.
     *
     * @param room - obiekt reprezentujący pokój
     * @return (int) - 0 w przypadku pomyślnego zakończenia aktualizowania danych pokoju
     * kod błędu w przypadku błędu
     */
    int updateRoom(Room room);

    /**
     * Metoda do obsługi usuwania pokoju.
     *
     * @param id - id pokoju
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int deleteRoom(long id);
}
