package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Doctors.Director;
import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
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
                return getJdbcTemplate().query("SELECT * FROM SZPITALE", (rs, arg1) -> new Hospital(
                        rs.getLong("id_szpitala"), rs.getString("nazwa_szpitala"),
                        rs.getString("adres"), rs.getString("miasto")));
            }

            @Override
            public Hospital getHospitalById(long id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM SZPITALE WHERE ID_SZPITALA = " + id,
                            (rs, ag1) -> new Hospital(rs.getLong("id_szpitala"), rs.getString("nazwa_szpitala"),
                                    rs.getString("adres"), rs.getString("miasto")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public Director getHospitalDirector(long hospitalId) {
                try {
                    return getJdbcTemplate().queryForObject("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.id_oddzialu, l.stanowisko " +
                                    "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                    "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                    "where  o.ID_SZPITALA = " + hospitalId + " and l.stanowisko = 'Dyrektor' order by s.id_szpitala",
                            (rs, ag1) -> new Director(hospitalId, rs.getLong("id_lekarza"), rs.getString("imie"),
                                    rs.getString("nazwisko"), rs.getLong("id_oddzialu"))
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
                                "where l.stanowisko = 'Dyrektor' order by s.id_szpitala",
                        (rs, ag1) -> new Director(rs.getLong("id_szpitala"), rs.getLong("id_lekarza"),
                                rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getLong("id_oddzialu")));
            }

            @Override
            public List<Ordynator> getHospitalOrdynators(long hospitalId) {
                return getJdbcTemplate().query("select o.nazwa, s.id_szpitala, l.ID_LEKARZA, " +
                                "l.IMIE, l.NAZWISKO, l.id_oddzialu from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where  o.ID_SZPITALA = 1 and l.stanowisko = 'Ordynator'",
                        (rs, arg1) -> new Ordynator(rs.getString("nazwa"), rs.getLong("id_szpitala"),
                                rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getLong("id_oddzialu")));
            }

            @Override
            public long insertHospital(Hospital hospital) {
                CallableStatement call = null;
                try {
                    call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertHospital(?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(5, OracleTypes.NUMBER);
                    call.setString(2, hospital.getName());
                    call.setString(3, hospital.getAddress());
                    call.setString(4, hospital.getCity());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            public int deleteHospital(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteHospital(?, ?)}"
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

            @Override
            public int updateHospital(Hospital hospital) {
                int rowCount = getJdbcTemplate().update(
                        "UPDATE SZPITALE SET " +
                                "nazwa_szpitala = '" + hospital.getName() + "', " +
                                "adres = '" + hospital.getAddress() + "', " +
                                "miasto = '" + hospital.getCity() + "' " +
                                "where id_szpitala = " + hospital.getId()
                );
                if (rowCount == 1) {
                    return 0;
                } else if (rowCount == 0) {
                    return -1;
                } else {
                    return -2;
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
     *
     * @return (String) - tekst w formacie JSON, który zawiera odpowiedź na żądanie
     * @See ResponseCreator
     */
    @RequestMapping(value = "/api/allHospitals")
    public String getHospitals() {
        JSONArray hospitalsArray = new JSONArray();
        List<Hospital> hospitals = hospitalInterface.getAllHospitals();
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
    @RequestMapping(value = "/api/hospital")
    public String getHospitalById(@RequestParam("id") long id) {
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

    /**
     * Metoda odpowiadająca za obsługę żądań dotyczących dyrektorów szpitali. Gdy hospitalId
     * nie zostanie podane zwraca dyrektorów wszystkich szpitali.
     *
     * @param hospitalId - id szpitala (jeśli nie podane przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON, który zawiera odpowiedź na żądanie.
     */
    @RequestMapping(value = "/api/hospitalDirector")
    public String getHospitalsDirectors(
            @RequestParam(value = "hospitalId", defaultValue = "-1", required = false) long hospitalId
    ) {
        if (hospitalId != -1) {
            Director director = hospitalInterface.getHospitalDirector(hospitalId);
            try {
                if (director != null) {
                    JSONObject doctorObject = director.toJSONObject();
                    return ResponseCreator.jsonResponse("director", doctorObject,
                            "Director of hospital with id = " + hospitalId);
                } else {
                    return ResponseCreator.jsonErrorResponse("No director of hospital with id = " + hospitalId);
                }
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


    /**
     * Metoda odpowiadająca za obsługę żądania informacji o wszystkich ordynatorach w danym szpitalu
     *
     * @param hospitalId - id szpitala
     * @return (String) - tekst w formacie json zawierający odpowiedź na żądanie.
     */
    @RequestMapping(value = "/api/hospitalOrdynators")
    public String getHospitalOrdynators(@RequestParam("hospitalId") long hospitalId) {
        List<Ordynator> ordynators = hospitalInterface.getHospitalOrdynators(hospitalId);
        JSONArray ordynatorsArray = new JSONArray();
        for (Ordynator ordynator : ordynators) {
            try {
                ordynatorsArray.add(ordynator.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("ordynators", ordynatorsArray,
                "List of ordynators in hospital with id = " + hospitalId);
    }

    @RequestMapping(value = "/api/addHospital", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addHospital(@RequestBody String hospitalData) {
        try {
            Hospital hospital = Hospital.getInstance(hospitalData);
            long id = hospitalInterface.insertHospital(hospital);
            if (id > 0) {
                hospital.setId(id);
                return ResponseCreator.jsonResponse("hospital", hospital.toJSONObject(),
                        "Successful adding hospital. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check hospital data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding element");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    @RequestMapping(value = "/api/updateHospital", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateHospital(@RequestBody String hospitalData) {
        try {
            Hospital hospital = Hospital.getInstance(hospitalData);
            int status = hospitalInterface.updateHospital(hospital);

            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating hospital with id = " + hospital.getId());
            } else if (status == -1) {
                return ResponseCreator.jsonErrorResponse("No hospital with id = " + hospital.getId());
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating hospital with id = " + hospital.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }


    @RequestMapping(value = "/api/deleteHospital", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteHospital(@RequestParam("id") long id) {
        int status = hospitalInterface.deleteHospital(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting hospital with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No hospital with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting hospital with id = " + id);
        }
    }


}
