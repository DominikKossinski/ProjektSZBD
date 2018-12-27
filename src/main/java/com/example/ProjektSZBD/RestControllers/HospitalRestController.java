package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Director;
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

            @Override
            public Director getHospitalDirector(int hospitalId) {
                try {
                    return getJdbcTemplate().queryForObject("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.id_oddzialu, l.stanowisko " +
                                    "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                    "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                    "where  o.ID_SZPITALA = " + hospitalId + " and l.stanowisko = 'Dyrektor'",
                            (rs, ag1) -> new Director(hospitalId, rs.getInt("id_lekarza"), rs.getString("imie"),
                                    rs.getString("nazwisko"), rs.getInt("id_oddzialu"))
                    );
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Director> getHospitalsDirectors() {
                return getJdbcTemplate().query("select s.id_szpitala, l.ID_LEKARZA, l.IMIE, l.NAZWISKO, " +
                                "l.id_oddzialu, l.stanowisko " +
                                "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where l.stanowisko = 'Dyrektor'",
                        (rs, ag1) -> new Director(rs.getInt("id_szpitala"), rs.getInt("id_lekarza"),
                                rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getInt("id_oddzialu")));
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
     *
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
     *
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

    @RequestMapping("/api/hospitalDirector")
    public String getHospitalsDirectors(
            @RequestParam(value = "hospitalId", defaultValue = "-1", required = false) int hospitalId
    ) {
        if (hospitalId != -1) {
            Director director = hospitalInterface.getHospitalDirector(hospitalId);
            try {
                JSONObject doctorObject = director.toSimpleJSONObject();
                return ResponseCreator.jsonResponse("director", doctorObject,
                        "Director of hospital with id = " + hospitalId);
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        } else {
            List<Director> directors = hospitalInterface.getHospitalsDirectors();
            JSONArray directorsArray = new JSONArray();
            for (Director director : directors) {
                try {
                    directorsArray.add(director.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("directors", directorsArray, "List of all directors");
        }
    }


}
