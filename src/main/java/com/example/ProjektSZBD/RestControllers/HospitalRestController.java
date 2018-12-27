package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController obsługujący żądania dotyczące szpitali, które znajdują się w bazie danych.
 */
@RestController
public class HospitalRestController {

    /**
     * Interfejs odpowiadający za komunikację z bazą danych.
     */
    private HospitalInterface hospitalInterface;

    /**
     * Publiczny konstruktor bez argumentowy, który tworzy domyślny interfejs do komunikacjii z bazą danych.
     */
    public HospitalRestController() {
        this.hospitalInterface = new HospitalInterface() {
            @Override
            public List<Hospital> getAllHospitals() {
                return getJdbcTemplate().query("SELECT * FROM SZPITALE", (rs, arg1) -> new Hospital(rs.getInt("id_szpitala"), rs.getString("nazwa_szpitala"),
                        rs.getString("adres"), rs.getString("miasto")));
            }

            @Override
            public Hospital getHospitalById(int id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM SZPITALE WHERE ID_SZPITALA = " + id,
                            (rs, ag1) -> new Hospital(rs.getInt("id_szpitala"), rs.getString("nazwa_szpitala"),
                                    rs.getString("adres"), rs.getString("miasto")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }
        };
    }

    /**
     * Publiczny konstruktor, przyjmujący jako argument interfejs odpowiadający za wystawianie
     * danych dla restController'a.
     *
     * @param hospitalInterface - interfejs odpowiadający za wystawianie danych o szpitalach.
     */
    public HospitalRestController(HospitalInterface hospitalInterface) {
        this.hospitalInterface = hospitalInterface;
    }

    /**
     * Metoda obsługująca żądanie danych o wszystkich szpitalach.
     * @return (String) - tekst w formacie JSON, który zawiera odpowiedź na żądanie
     * @See ResponseCreator
     */
    @RequestMapping("/api/allHospitals")
    public String getHospitals() {
        JSONArray hospitalsArray = new JSONArray();
        List<Hospital> hospitals = hospitalInterface.getAllHospitals();
        JSONParser parser = new JSONParser();
        for (Hospital hospital : hospitals) {
            try {
                JSONObject hospitalJsonObject = hospital.toJSONObject();
                hospitalsArray.add(hospitalJsonObject);
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("hospitals", hospitalsArray, "List of all hospitals");
    }

    /**
     * Metoda odpowiadająca za obsługę żądania dotyczącego danych o jednym szpitalu.
     * @param id - id szpitala, o którego dane zostało wysłane żądanie
     * @return (String) - tekst w formacie JSON, który zawiera odpowiedź na żądanie.
     * @See ResponseCreator
     */
    @RequestMapping("/api/hospital")
    public String getHospitalById(@RequestParam("id") int id) {
        Hospital hospital = hospitalInterface.getHospitalById(id);
        if (hospital == null) {
            return ResponseCreator.jsonErrorResponse("No hospital with id = " + id);
        } else {
            try {
                return ResponseCreator.jsonResponse("hospital",
                        hospital.toJSONObject(), "Details of hospital with id = " + id);
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
    }


}
