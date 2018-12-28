package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Illness;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.IllnessInterface;
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
                            return new Illness(rs.getInt("id_choroby"), rs.getString("nazwa"),
                                    rs.getString("opis"));
                        });
            }

            @Override
            public Illness getIllnessById(int id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM CHOROBY WHERE ID_CHOROBY = " + id,
                            (rs, arg1) -> {
                                return new Illness(rs.getInt("id_choroby"), rs.getString("nazwa"),
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
                            return new Illness(rs.getInt("id_choroby"), rs.getString("nazwa"),
                                    rs.getString("opis"));
                        });
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
            @RequestParam(name = "id", defaultValue = "-1", required = false) int id,
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
            JSONArray illnessesArray = new JSONArray();
            for (Illness illness : illnesses) {
                try {
                    illnessesArray.add(illness.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("illnesses", illnessesArray,
                    "List of all illnesses with pattern = " + pattern);
        } else {
            List<Illness> illnesses = illnessInterface.getAllIllnesses();
            JSONArray illnessesArray = new JSONArray();
            for (Illness illness : illnesses) {
                try {
                    illnessesArray.add(illness.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("illnesses", illnessesArray, "List of all illnesses");
        }
    }


}
