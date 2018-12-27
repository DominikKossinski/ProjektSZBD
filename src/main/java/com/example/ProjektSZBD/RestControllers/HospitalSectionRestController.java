package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.HospitalSection;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.HospitalSectionInterface;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController odpowiadający za żądania dotyczące oddziałów szpitalnych.
 */
@RestController
public class HospitalSectionRestController {

    /**
     * Interfejs odpowiadający za wystawianie danych dla RestController'a.
     */
    private HospitalSectionInterface hospitalSectionInterface;

    /**
     * Publiczny konstruktor bez argumentowy, który tworzy domyślny interfejs do komunikacji.
     * z bazą danych.
     */
    public HospitalSectionRestController() {
        this.hospitalSectionInterface = id -> {
            return getJdbcTemplate().query(
                    "SELECT * FROM ODDZIALY WHERE ID_SZPITALA = " + id,
                    (rs, arg1) -> {
                        return new HospitalSection(rs.getInt("id_oddzialu"),
                                rs.getString("nazwa"), rs.getInt("id_szpitala"));
                    });
        };
    }

    /**
     * Publiczny konstruktor klasy, który przyjmuje jako argument interfejs do wystawiania danych dla RestController'a.
     *
     * @param hospitalSectionInterface - interfejs wystawiający dane dla RestController'a.
     */
    public HospitalSectionRestController(HospitalSectionInterface hospitalSectionInterface) {
        this.hospitalSectionInterface = hospitalSectionInterface;
    }

    /**
     * Metoda klasy odpowiadająca za obsługę żądania wszystkich oddziałów w danym szpitalu.
     *
     * @param hospitalId - id szpitala, o którego oddziały żądanie zostało wysłane
     * @return (String) - tekst w formacie json zawierający odpowiedź na żądanie.
     */
    @GetMapping("/api/hospitalSections")
    public String getHospitalSectionsByHospitalId(@RequestParam("hospitalId") int hospitalId) {
        List<HospitalSection> hospitalSections = hospitalSectionInterface.getHospitalSectionsByHospitalId(hospitalId);
        JSONArray sectionsArray = new JSONArray();
        for (HospitalSection section : hospitalSections) {
            try {
                sectionsArray.add(section.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("sections", sectionsArray,
                "List of sections in hospital with id =" + hospitalId);


    }


}
