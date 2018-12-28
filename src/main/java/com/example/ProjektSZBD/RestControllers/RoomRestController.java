package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Room;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.RoomInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController odpowiadający za obsługę żądań związanych danymi o pokojach.
 */
@RestController
public class RoomRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane o pokojach.
     */
    private RoomInterface roomInterface;

    /**
     * Publiczny konstruktor ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public RoomRestController() {
        this.roomInterface = new RoomInterface() {
            @Override
            public Room getRoomById(int id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM POKOJE WHERE ID_POKOJU = " + id,
                            (rs, arg1) -> new Room(rs.getInt("id_pokoju"), rs.getInt("pietro"),
                                    rs.getInt("liczba_miejsc"), rs.getInt("id_oddzialu"),
                                    rs.getInt("ilosc_zajetych_miejsc")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Room> getRoomsByHospitalSectionId(int hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM POKOJE WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Room(rs.getInt("id_pokoju"), rs.getInt("pietro"),
                                rs.getInt("liczba_miejsc"), rs.getInt("id_oddzialu"),
                                rs.getInt("ilosc_zajetych_miejsc")));
            }

            @Override
            public List<Room> getRoomsWithFreePlaces(int hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM POKOJE WHERE ilosc_zajetych_miejsc < liczba_miejsc " +
                                "and  ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Room(rs.getInt("id_pokoju"), rs.getInt("pietro"),
                                rs.getInt("liczba_miejsc"), rs.getInt("id_oddzialu"),
                                rs.getInt("ilosc_zajetych_miejsc")));
            }
        };
    }

    /**
     * Publiczny konstruktor ustawiający interfejs na interfejs przekazany jako argument.
     *
     * @param roomInterface - interfejs wystawiający dane.
     */
    public RoomRestController(RoomInterface roomInterface) {
        this.roomInterface = roomInterface;
    }


    /**
     * Metoda zwracająca informacje na temat pokojów. Zwraca pokój o podanym id,
     * jeśli zostnie podane id, listę pokojów jeśli zostanie podane id oddziału. Dodatkowo
     * listę wolnych pokojów, jeśli do parametru hospitalId zostanie dodany parametr free z wartością true.
     *
     * @param id                - id pokoju (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param free              - zmienna boolowska (jeśli nie zostało podane to przyjmuje wartość false)
     * @param hospitalSectionId - id oddzialu (jeśli nie zostało podane to przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych pokojach
     */
    @RequestMapping("/api/rooms")
    public String getRooms(
            @RequestParam(value = "id", defaultValue = "-1", required = false) int id,
            @RequestParam(value = "free", defaultValue = "false", required = false) boolean free,
            @RequestParam(value = "hospitalSectionId", defaultValue = "-1", required = false) int hospitalSectionId
    ) {
        if (id != -1) {
            Room room = roomInterface.getRoomById(id);
            if (room != null) {
                try {
                    JSONObject roomObject = room.toJSONObject();
                    return ResponseCreator.jsonResponse("room", roomObject, "Room with id = " + id);
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            } else {
                return ResponseCreator.jsonErrorResponse("No room with id = " + id);
            }
        } else {
            if (hospitalSectionId != -1) {
                List<Room> rooms;
                if (free) {
                    rooms = roomInterface.getRoomsWithFreePlaces(hospitalSectionId);
                    JSONArray roomsArray = new JSONArray();
                    for (Room room : rooms) {
                        try {
                            roomsArray.add(room.toJSONObject());
                        } catch (ParseException e) {
                            return ResponseCreator.parseErrorResponse(e);
                        }
                    }
                    return ResponseCreator.jsonResponse("rooms", roomsArray,
                            "List of free rooms in hospital section with id = " + hospitalSectionId);
                } else {
                    rooms = roomInterface.getRoomsByHospitalSectionId(hospitalSectionId);
                    JSONArray roomsArray = new JSONArray();
                    for (Room room : rooms) {
                        try {
                            roomsArray.add(room.toJSONObject());
                        } catch (ParseException e) {
                            return ResponseCreator.parseErrorResponse(e);
                        }
                    }
                    return ResponseCreator.jsonResponse("rooms", roomsArray,
                            "List of rooms in hospital section with id = " + hospitalSectionId);
                }

            } else {
                return ResponseCreator.jsonErrorResponse("You have to specify hospitalSectionId");
            }
        }
    }
}
