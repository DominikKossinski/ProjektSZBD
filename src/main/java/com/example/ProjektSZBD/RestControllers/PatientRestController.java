package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Patient;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.PatientInterface;
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
 * RestController odpowiadający za obsługę żądań związanych z pacjentami.
 */
@RestController
public class PatientRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane o pacjentach.
     */
    private PatientInterface patientInterface;

    /**
     * Publiczny konstruktor ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public PatientRestController() {
        this.patientInterface = new PatientInterface() {
            @Override
            public Patient getPatientByPesel(long pesel) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM PACJENCI WHERE PESEL = " + pesel,
                            (rs, arg1) -> {
                                return new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                        rs.getString("nazwisko"), rs.getString("haslo"));
                            });
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Patient> getAllPatients() {

                return getJdbcTemplate().query("SELECT * FROM PACJENCI",
                        (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getString("haslo")));
            }

            @Override
            public Patient getPatientInfo(long pesel) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT pesel, imie, nazwisko FROM PACJENCI where pesel = " + pesel,
                            (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                    rs.getString("nazwisko")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Patient> getAllPatientsInfo() {
                return getJdbcTemplate().query("SELECT pesel, imie, nazwisko FROM PACJENCI",
                        (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                rs.getString("nazwisko")));
            }
        };
    }

    /**
     * Publiczny konstruktor klasy ustawiający interfejs przekazany jako argument.
     *
     * @param patientInterface - interfejs wystawiający dane o pacjentach
     */
    public PatientRestController(PatientInterface patientInterface) {
        this.patientInterface = patientInterface;
    }

    /**
     * Metoda zwracająca pełne dane o pacjentach lub o jednym pacjencie, gdy podamy jego pesel.
     *
     * @param pesel - pesel (domyślnie równy -1)
     * @return (String) - tekst w formacie JSON zawierający pełne dane o pacjentach
     */
    @RequestMapping("/api/patient")
    public String getPatients(@RequestParam(value = "pesel", defaultValue = "-1", required = false) long pesel) {
        if (pesel == -1) {
            List<Patient> patients = patientInterface.getAllPatients();
            JSONArray patientsArray = new JSONArray();
            for (Patient patient : patients) {
                try {
                    patientsArray.add(patient.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("patients", patientsArray, "List of all patients");
        } else {
            Patient patient = patientInterface.getPatientByPesel(pesel);
            try {
                if (patient != null) {
                    JSONObject patientObject = patient.toJSONObject();
                    return ResponseCreator.jsonResponse("patient", patientObject, "Patient with pesel = " + pesel);
                } else {
                    return ResponseCreator.jsonErrorResponse("No patient with pesel = " + pesel);
                }
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
    }

    /**
     * Metoda zwracająca podstawowe dane o pacjentach lub o jednym pacjencie, gdy podamy jego pesel.
     *
     * @param pesel - pesel (domyślnie równy -1)
     * @return (String) - tekst w formacie JSON zawierający podstawowe dane o pacjentach
     */
    @RequestMapping("/api/patientInfo")
    public String getPatientsInfo(@RequestParam(value = "pesel", defaultValue = "-1", required = false) long pesel) {
        if (pesel == -1) {
            List<Patient> patients = patientInterface.getAllPatientsInfo();
            JSONArray patientsArray = new JSONArray();
            for (Patient patient : patients) {
                try {
                    patientsArray.add(patient.toSimpleJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("patients", patientsArray, "List of all patients");
        } else {
            Patient patient = patientInterface.getPatientInfo(pesel);
            if (patient != null) {
                try {
                    JSONObject patientObject = patient.toSimpleJSONObject();
                    return ResponseCreator.jsonResponse("patient", patientObject, "Patient with pesel = " + pesel);
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            } else {
                return ResponseCreator.jsonErrorResponse("No patient with pesel = " + pesel);
            }
        }
    }
}
