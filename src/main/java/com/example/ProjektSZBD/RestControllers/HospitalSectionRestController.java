package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.HospitalSection;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.HospitalSectionInterface;
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
        this.hospitalSectionInterface = new HospitalSectionInterface() {
            @Override
            public List<HospitalSection> getHospitalSectionsByHospitalId(long id) {
                return getJdbcTemplate().query(
                        "SELECT * FROM ODDZIALY WHERE ID_SZPITALA = " + id,
                        (rs, arg1) -> new HospitalSection(rs.getLong("id_oddzialu"),
                                rs.getString("nazwa"), rs.getLong("id_szpitala")));
            }

            @Override
            public HospitalSection getHospitalSectionById(long id) {
                try {
                    return getJdbcTemplate().queryForObject(
                            "SELECT * FROM ODDZIALY WHERE ID_ODDZIALU = " + id,
                            (rs, arg1) -> new HospitalSection(rs.getLong("id_oddzialu"),
                                    rs.getString("nazwa"), rs.getLong("id_szpitala")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public Ordynator getHospitalSectionOrdynator(long hospitalSectionId) {
                try {
                    return getJdbcTemplate().queryForObject("select o.nazwa, s.id_szpitala, l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.id_oddzialu " +
                                    "from lekarze l " +
                                    "       join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                    "       join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                    "where o.id_oddzialu = 1 " +
                                    "  and l.stanowisko = 'Ordynator'",
                            (rs, arg1) -> new Ordynator(rs.getString("nazwa"), rs.getLong("id_szpitala"),
                                    rs.getLong("id_lekarza"), rs.getString("imie"),
                                    rs.getString("nazwisko"), rs.getLong("id_oddzialu")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public long insertHospitalSection(HospitalSection hospitalSection) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertHospitalSection(?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(4, OracleTypes.NUMBER);
                    call.setString(2, hospitalSection.getName());
                    call.setLong(3, hospitalSection.getHospitalId());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateHospitalSection(HospitalSection hospitalSection) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateHospitalSection(?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(5, OracleTypes.NUMBER);
                    call.setLong(2, hospitalSection.getId());
                    call.setString(3, hospitalSection.getName());
                    call.setLong(4, hospitalSection.getHospitalId());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            public int deleteHospitalSection(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteHospitalSection(?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(3, OracleTypes.NUMBER);
                    call.setLong(2, id);
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
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
    @RequestMapping(value = "/api/hospitalSections", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getHospitalSectionsByHospitalId(@RequestParam("hospitalId") long hospitalId) {
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
                "List of sections in hospital with id = " + hospitalId);


    }

    /**
     * Metoda odpowiadająca za obsługę żądania odziału o podanym id.
     *
     * @param id - id oddziału
     * @return String) - tekst w formacie json zawierający odpowiedź na żądanie.
     */
    @RequestMapping(value = "/api/hospitalSection", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getHospitalSectionById(@RequestParam("id") long id) {
        HospitalSection section = hospitalSectionInterface.getHospitalSectionById(id);
        if (section != null) {
            try {
                return ResponseCreator.jsonResponse("hospital_section", section.toJSONObject(),
                        "Hospital section with id = " + id);
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        } else {
            return ResponseCreator.jsonErrorResponse("No hospital section with id = " + id);
        }
    }

    /**
     * Metoda odpowiadająca za żądanie informacji o ordynatorze oddziału.
     *
     * @param hospitalSectionId - id oddziału
     * @return (String) - tekst w formacie json zawierający odpowiedź na żądanie.
     */
    @RequestMapping("/api/sectionOrdynator")
    public String getSectionOrdynator(@RequestParam("hospitalSectionId") long hospitalSectionId) {
        Ordynator ordynator = hospitalSectionInterface.getHospitalSectionOrdynator(hospitalSectionId);
        try {
            if (ordynator != null) {
                JSONObject ordynatorObject = ordynator.toJSONObject();
                return ResponseCreator.jsonResponse("ordynator", ordynatorObject,
                        "Ordynator of hospital section with id = " + hospitalSectionId);
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "No ordynator of hospitalSection with id = " + hospitalSectionId);
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania wstawiania oddziału.
     *
     * @param hospitalSectionData - tekst w formacie JSON zawierający dane o oddziale
     * @return (String) - odpowiedź serwera zawierająca status zakończenia wstawiania oddzialu
     */
    @RequestMapping(value = "/api/addHospitalSection", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addHospitalSection(@RequestBody String hospitalSectionData) {
        try {
            HospitalSection hospitalSection = HospitalSection.getInstance(hospitalSectionData);
            long id = hospitalSectionInterface.insertHospitalSection(hospitalSection);
            if (id > 0) {
                hospitalSection.setId(id);
                return ResponseCreator.jsonResponse("hospital_section", hospitalSection.toJSONObject(),
                        "Successful adding hospital section. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check hospital section data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding hospital section");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania aktualizowania danych oddziału.
     *
     * @param hospitalSectionData - tekst w formacie JSON zawierający dane o oddziale
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych oddzialu
     */
    @RequestMapping(value = "/api/updateHospitalSection", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateHospitalSection(@RequestBody String hospitalSectionData) {
        try {
            HospitalSection hospitalSection = HospitalSection.getInstance(hospitalSectionData);
            int status = hospitalSectionInterface.updateHospitalSection(hospitalSection);

            if (status == 0) {
                return ResponseCreator.jsonResponse(
                        "Successful updating hospital section with id = " + hospitalSection.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No hospital section with id = " + hospitalSection.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating hospital section with id = " + hospitalSection.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }


    /**
     * Metoda odpowiadająca za obsługę żądania usuwania oddziału.
     *
     * @param id - id oddziału
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania oddzialu
     */
    @RequestMapping(value = "/api/deleteHospitalSection", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteHospitalSection(@RequestParam("id") long id) {
        int status = hospitalSectionInterface.deleteHospitalSection(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting hospital section with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No hospital section with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting hospital section with id = " + id);
        }
    }


}
