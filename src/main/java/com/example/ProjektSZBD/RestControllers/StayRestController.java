package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Stay;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.StayInterface;
import oracle.jdbc.OracleTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController odpowiadający za obsługę żądań dotyczących pobytów.
 */
@RestController
public class StayRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane.
     */
    private StayInterface stayInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public StayRestController() {
        this.stayInterface = new StayInterface() {
            @Override
            public Stay getStayById(long id) {
                return getJdbcTemplate().queryForObject("SELECT * from pobyty where id_pobytu = " + id,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }

            @Override
            public List<Stay> getStayByPesel(long pesel) {
                return getJdbcTemplate().query("SELECT * from pobyty where pesel = " + pesel,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }

            @Override
            public List<Stay> getStayByDoctor(long doctorId) {
                return getJdbcTemplate().query("SELECT * from pobyty where id_lekarza = " + doctorId,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }

            @Override
            public long insertStay(Stay stay) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertStay(?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(7, OracleTypes.NUMBER);
                    call.setDate(2, stay.getStartDate());
                    call.setDate(3, stay.getEndDate());
                    call.setLong(4, stay.getRoomId());
                    call.setLong(5, stay.getDoctorId());
                    call.setLong(6, stay.getPesel());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateStay(Stay stay) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateStay(?, ?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(8, OracleTypes.NUMBER);
                    call.setLong(2, stay.getId());
                    call.setDate(3, stay.getStartDate());
                    call.setDate(4, stay.getEndDate());
                    call.setLong(5, stay.getRoomId());
                    call.setLong(6, stay.getDoctorId());
                    call.setLong(7, stay.getPesel());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int deleteStay(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteStay(?, ?)}"
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
     * Publiczny konstruktor klasy ustawiający interfejs na interfejs przekazany przez parametr.
     *
     * @param stayInterface - interfejs wystawiający dane
     */
    public StayRestController(StayInterface stayInterface) {
        this.stayInterface = stayInterface;
    }

    /**
     * Metoda zwracająca informacje o pobytach. Jeśli id zostanie podane zwróci informacje o pobycie
     * z podanym id. Jeśli zostanie podany pesel zwróci informacje o pobytach pacjenta. Jeśli zostanie
     * podene id lekarza zostanie zwrócona lista pobytów które utworzył dany lekarz.
     *
     * @param id       - id pobytu (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param pesel    - pesel pacjenta (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param doctorId - id lekarza (jeśli nie zostało podane to przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych pobytach
     */
    @RequestMapping(value = "/api/stays", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getStays(
            @RequestParam(value = "id", defaultValue = "-1", required = false) long id,
            @RequestParam(value = "pesel", defaultValue = "-1", required = false) long pesel,
            @RequestParam(value = "doctorId", defaultValue = "-1", required = false) long doctorId
    ) {
        if (id != -1) {
            Stay stay = stayInterface.getStayById(id);
            if (stay != null) {
                try {
                    JSONObject stayObject = stay.toJSONObject();
                    return ResponseCreator.jsonResponse("stay", stayObject, "Stay with id = " + id);
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }

            } else {
                return ResponseCreator.jsonErrorResponse("No stay with id = " + id);
            }
        }
        if (pesel != -1) {
            List<Stay> stays = stayInterface.getStayByPesel(pesel);
            return createResponseWithStaysList(stays, "Stays of patient with pesel = " + pesel);
        }
        if (doctorId != -1) {
            List<Stay> stays = stayInterface.getStayByDoctor(doctorId);
            return createResponseWithStaysList(stays, "Stays with doctorId = " + doctorId);
        }
        return ResponseCreator.jsonErrorResponse(
                "You need to specify one of parameters [id, pesel, doctorId]");
    }

    /**
     * Metoda do obsługi żądania pobytów pacjenta o podanym peselu.
     *
     * @param pesel - pesel
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych pobytach
     */
    @RequestMapping(value = "/api/patient/{pesel}/stays", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getMyStays(@PathVariable("pesel") long pesel) {
        List<Stay> stays = stayInterface.getStayByPesel(pesel);
        return createResponseWithStaysList(stays, "Stays of patient with pesel = " + pesel);
    }


    /**
     * Metoda odpowiadająca za obsługę żądań wstawiania pobytu.
     *
     * @param stayData - tekst w formacie JSON zawierający dane o pobycie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania pobytu
     */
    @RequestMapping(value = "/api/addStay", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertStay(@RequestBody String stayData) {
        try {
            Stay stay = Stay.getInstance(stayData);
            long id = stayInterface.insertStay(stay);
            if (id > 0) {
                stay.setId(id);
                return ResponseCreator.jsonResponse("stay", stay.toJSONObject(),
                        "Successful adding stay. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check stay data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding stay");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizacji danych pobytu.
     *
     * @param stayData - tekst w formacie JSON zawierający dane o pobycie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych pokoju
     */
    @RequestMapping(value = "/api/updateStay", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateStay(@RequestBody String stayData) {
        try {
            Stay stay = Stay.getInstance(stayData);
            int status = stayInterface.updateStay(stay);

            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating stay with id = " + stay.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No stay with id = " + stay.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating stay with id = " + stay.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    @RequestMapping(value = "/api/deleteStay", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteStay(@RequestParam("id") long id) {
        int status = stayInterface.deleteStay(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting stay with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No stay with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting stay with id = " + id);
        }
    }

    /**
     * Metoda zwracająca odpowiedź serwera na podstawie listy pobytów i jej opisu.
     *
     * @param stays       - lista pobytów
     * @param description - opis
     * @return (String) - tekst zawierający odpowiedź serwera w formacie JSON
     */
    private String createResponseWithStaysList(List<Stay> stays, String description) {
        JSONArray staysArray = new JSONArray();
        for (Stay stay : stays) {
            try {
                staysArray.add(stay.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("stays", staysArray,
                description);
    }
}
