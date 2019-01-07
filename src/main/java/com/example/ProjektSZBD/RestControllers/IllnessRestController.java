package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Illness;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.IllnessInterface;
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
 * RestController odpowiadający za obsługę żądań związanych z chorobami.
 */
@RestController
public class IllnessRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane o chorobach.
     */
    private IllnessInterface illnessInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public IllnessRestController() {
        this.illnessInterface = new IllnessInterface() {
            @Override
            public List<Illness> getAllIllnesses() {
                return getJdbcTemplate().query("SELECT * FROM CHOROBY",
                        (rs, arg1) -> {
                            return new Illness(rs.getLong("id_choroby"), rs.getString("nazwa"),
                                    rs.getString("opis"));
                        });
            }

            @Override
            public Illness getIllnessById(long id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM CHOROBY WHERE ID_CHOROBY = " + id,
                            (rs, arg1) -> {
                                return new Illness(rs.getLong("id_choroby"), rs.getString("nazwa"),
                                        rs.getString("opis"));
                            });
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }

            }

            @Override
            public List<Illness> getAllIllnessesWithPattern(String pattern) {
                return getJdbcTemplate().query("SELECT * FROM CHOROBY WHERE LOWER(nazwa) like '%" + pattern + "%'",
                        (rs, arg1) -> {
                            return new Illness(rs.getLong("id_choroby"), rs.getString("nazwa"),
                                    rs.getString("opis"));
                        });
            }

            @Override
            public long insertIllness(Illness illness) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertIllness(?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(4, OracleTypes.NUMBER);
                    call.setString(2, illness.getName());
                    call.setString(3, illness.getDescription());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateIllness(Illness illness) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateIllness(?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(5, OracleTypes.NUMBER);
                    call.setLong(2, illness.getId());
                    call.setString(3, illness.getName());
                    call.setString(4, illness.getDescription());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int deleteIllness(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteIllness(?, ?)}"
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
     * Publiczny konstruktor klasy ustawiający interfejs na interfejs przekazany jako argument.
     *
     * @param illnessInterface - interfejs wystawiający dane o chorobach
     */
    public IllnessRestController(IllnessInterface illnessInterface) {
        this.illnessInterface = illnessInterface;
    }

    /**
     * Metoda zwracająca informacje o chorobach, gdy parametry są puste zwraca informacje o wszystkich chorobach.
     * W przypadku podania id wyszukiwana jest choroba o podanym id. W przypadku podania parametru pattern
     * wyszukiwana jest choroba, w której nazwie pojawia się podany ciąg znaków.
     *
     * @param id      - id choroby (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param pattern - ciąg znaków (jeśli nie zostało podane to przyjmuje wartość ciągu pustego)
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych chorobach
     */
    @RequestMapping("/api/illness")
    public String getIllnesses(
            @RequestParam(name = "id", defaultValue = "-1", required = false) long id,
            @RequestParam(name = "pattern", defaultValue = "", required = false) String pattern
    ) {
        if (id != -1) {
            Illness illness = illnessInterface.getIllnessById(id);
            try {
                if (illness != null) {
                    JSONObject illnessObject = illness.toJSONObject();
                    return ResponseCreator.jsonResponse("illness", illnessObject, "Illness with id = " + id);
                } else {
                    return ResponseCreator.jsonErrorResponse("No illness with id = " + id);
                }
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        } else if (!pattern.isEmpty()) {
            List<Illness> illnesses = illnessInterface.getAllIllnessesWithPattern(pattern.toLowerCase());
            return createResponseWithIllnessesList(illnesses, "List of all illnesses with pattern = " + pattern);
        } else {
            List<Illness> illnesses = illnessInterface.getAllIllnesses();
            return createResponseWithIllnessesList(illnesses, "List of all illnesses");
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania wstawiania choroby.
     *
     * @param illnessData - tekst w formacie JSON zawierający dane o chorobie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia wstawiania choroby
     */
    @RequestMapping(value = "/api/addIllness", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertIllness(@RequestBody String illnessData) {
        try {
            Illness illness = Illness.getInstance(illnessData);
            long id = illnessInterface.insertIllness(illness);
            if (id > 0) {
                illness.setId(id);
                return ResponseCreator.jsonResponse("illness", illness.toJSONObject(),
                        "Successful adding illness. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check illness data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding illness");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania aktualizowania danych choroby.
     *
     * @param illnessData - tekst w formacie JSON zawierający dane o chorobie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych choroby
     */
    @RequestMapping(value = "/api/updateIllness", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateIllness(@RequestBody String illnessData) {
        try {
            Illness illness = Illness.getInstance(illnessData);
            int status = illnessInterface.updateIllness(illness);

            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating illness with id = " + illness.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No illness with id = " + illness.getId());
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating illness with id = " + illness.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania usunięcia choroby.
     *
     * @param id - id choroby
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania choroby
     */
    @RequestMapping(value = "/api/deleteIllness", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteIllness(@RequestParam("id") long id) {
        int status = illnessInterface.deleteIllness(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting illness with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No illness with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting illness with id = " + id);
        }
    }


    /**
     * Metoda do przygotowania odpowiedzi serwera zawierającego listę chorób.
     *
     * @param illnesses   - lista chorób
     * @param description - opis
     * @return (String) - tekst w formacie JSON zawierający odpowiedź serwera
     */
    private String createResponseWithIllnessesList(List<Illness> illnesses, String description) {
        JSONArray illnessesArray = new JSONArray();
        for (Illness illness : illnesses) {
            try {
                illnessesArray.add(illness.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("illnesses", illnessesArray, description);
    }


}
