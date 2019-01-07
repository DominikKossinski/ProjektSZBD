package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Room;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.RoomInterface;
import oracle.jdbc.OracleTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
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
            public Room getRoomById(long id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM POKOJE WHERE ID_POKOJU = " + id,
                            (rs, arg1) -> new Room(rs.getLong("id_pokoju"), rs.getInt("pietro"),
                                    rs.getInt("liczba_miejsc"), rs.getLong("id_oddzialu"),
                                    rs.getInt("ilosc_zajetych_miejsc")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Room> getRoomsByHospitalSectionId(long hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM POKOJE WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Room(rs.getLong("id_pokoju"), rs.getInt("pietro"),
                                rs.getInt("liczba_miejsc"), rs.getLong("id_oddzialu"),
                                rs.getInt("ilosc_zajetych_miejsc")));
            }

            @Override
            public List<Room> getRoomsWithFreePlaces(long hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM POKOJE WHERE ilosc_zajetych_miejsc < liczba_miejsc " +
                                "and  ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Room(rs.getLong("id_pokoju"), rs.getInt("pietro"),
                                rs.getInt("liczba_miejsc"), rs.getLong("id_oddzialu"),
                                rs.getInt("ilosc_zajetych_miejsc")));
            }

            @Override
            public long insertRoom(Room room) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertRoom(?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(6, OracleTypes.NUMBER);
                    call.setInt(2, room.getFloor());
                    call.setInt(3, room.getNumberOfPlaces());
                    call.setLong(4, room.getHospitalSectionId());
                    call.setInt(5, room.getActPlacedCount());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateRoom(Room room) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateRoom(?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(7, OracleTypes.NUMBER);
                    call.setLong(2, room.getId());
                    call.setInt(3, room.getFloor());
                    call.setInt(4, room.getNumberOfPlaces());
                    call.setLong(5, room.getHospitalSectionId());
                    call.setInt(6, room.getActPlacedCount());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int deleteRoom(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteRoom(?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(3, OracleTypes.NUMBER);
                    call.setLong(2, id);
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
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
    @RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getRooms(
            @RequestParam(value = "id", defaultValue = "-1", required = false) long id,
            @RequestParam(value = "free", defaultValue = "false", required = false) boolean free,
            @RequestParam(value = "hospitalSectionId", defaultValue = "-1", required = false) long hospitalSectionId
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
                    return createResponseWithRoomsList(rooms,
                            "List of free rooms in hospital section with id = " + hospitalSectionId);
                } else {
                    rooms = roomInterface.getRoomsByHospitalSectionId(hospitalSectionId);
                    return createResponseWithRoomsList(rooms,
                            "List of rooms in hospital section with id = " + hospitalSectionId);
                }

            } else {
                return ResponseCreator.jsonErrorResponse("You have to specify hospitalSectionId");
            }
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań wstawiania pokoju.
     *
     * @param roomData - tekst w formacie JSON zawierający dane o pokoju
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania pokoju
     */
    @RequestMapping(value = "/api/addRoom", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertRoom(@RequestBody String roomData) {
        try {
            Room room = Room.getInstance(roomData);
            long id = roomInterface.insertRoom(room);
            if (id > 0) {
                room.setId(id);
                return ResponseCreator.jsonResponse("room", room.toJSONObject(),
                        "Successful adding room. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check room data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding room");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizacji danych pokoju.
     *
     * @param roomData - tekst w formacie JSON zawierający dane o pokoju
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych pokoju
     */
    @RequestMapping(value = "/api/updateRoom", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateRoom(@RequestBody String roomData) {
        try {
            Room room = Room.getInstance(roomData);
            int status = roomInterface.updateRoom(room);

            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating room with id = " + room.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No room with id = " + room.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating room with id = " + room.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za żądania usunięcia pokoju.
     *
     * @param id - id pokoju
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania pokoju
     */
    @RequestMapping(value = "/api/deleteRoom", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteRoom(@RequestParam("id") long id) {
        int status = roomInterface.deleteRoom(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting room with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No room with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting room with id = " + id);
        }
    }

    /**
     * Metoda zwracająca odpowiedź serwera na podstawie listy pokoi i jej opisu.
     *
     * @param rooms       - lista pokoi
     * @param description - opis
     * @return (String) - tekst zawierający odpowiedź serwera w formacie JSON
     */
    private String createResponseWithRoomsList(List<Room> rooms, String description) {
        JSONArray roomsArray = new JSONArray();
        for (Room room : rooms) {
            try {
                roomsArray.add(room.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("rooms", roomsArray,
                description);
    }
}
