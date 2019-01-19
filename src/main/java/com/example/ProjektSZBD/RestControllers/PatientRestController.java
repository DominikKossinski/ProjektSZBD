package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Patient;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.PatientInterface;
import oracle.jdbc.OracleTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getInMemoryUserDetailsManager;
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

                return getJdbcTemplate().query("SELECT * FROM PACJENCI" + " order by PESEL",
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
                return getJdbcTemplate().query("SELECT pesel, imie, nazwisko FROM PACJENCI" + " order by PESEL",
                        (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                rs.getString("nazwisko")));
            }

            @Override
            public List<Patient> getPatientsByPattern(String pattern) {
                return getJdbcTemplate().query("SELECT * FROM PACJENCI " +
                                "WHERE TO_CHAR(PESEL) LIKE '%" + pattern + "%' OR" +
                                " LOWER(IMIE) LIKE '%" + pattern + "%' OR" +
                                " Lower(NAZWISKO) LIKE '%" + pattern + "%'" +
                                " order by PESEL",
                        (rs, arg1) -> new Patient(rs.getLong("pesel"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getString("haslo")));
            }

            @Override
            public int insertPatient(Patient patient) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertPatient(?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.setLong(2, patient.getPesel());
                    call.setString(3, patient.getFirstName());
                    call.setString(4, patient.getLastName());
                    call.setString(5, patient.getPassword());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updatePatient(Patient patient) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updatePatient(?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(6, OracleTypes.NUMBER);
                    call.setLong(2, patient.getPesel());
                    call.setString(3, patient.getFirstName());
                    call.setString(4, patient.getLastName());
                    call.setString(5, patient.getPassword());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int deletePatient(long pesel) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deletePatient(?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(3, OracleTypes.NUMBER);
                    call.setLong(2, pesel);
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
            return createResponseWithPatient(patient, pesel);
        }
    }

    /**
     * Metoda zwracająca dane pacjenta na podstawie peselu.
     *
     * @param pesel - pesel pacjenta
     * @return (String) - tekst w formacie JSON zawierający pełne dane o pacjencie
     */
    @RequestMapping(value = "/api/patient/{pesel}/patient", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPatientByPesel(@PathVariable("pesel") long pesel) {
        Patient patient = patientInterface.getPatientByPesel(pesel);
        return createResponseWithPatient(patient, pesel);

    }

    /**
     * Metoda zwracająca dane pacjenta na podstawie ciągu znaków.
     *
     * @param pattern - ciąg znaków
     * @return (String) - tekst w formacie JSON zawierający pełne dane o znalezionych pacjentach
     */
    @RequestMapping(value = "/api/searchPatients", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String searchPatientsByPattern(@RequestParam(name = "pattern") String pattern) {
        JSONArray patientsArray = new JSONArray();
        List<Patient> patients = patientInterface.getPatientsByPattern(pattern.toLowerCase());
        for (Patient patient : patients) {
            try {
                patientsArray.add(patient.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("patients", patientsArray,
                "List of patients with pattern: " + pattern);
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

    /**
     * Metoda obsługująca żądanie dodania pacjenta.
     *
     * @param patientData - tekst w formacie JSON zawierający informacje o pacjencie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania pacjenta
     */
    @RequestMapping(value = "/api/addPatient", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertPatient(@RequestBody String patientData) {
        try {
            Patient patient = Patient.getInstance(patientData);
            int status = patientInterface.insertPatient(patient);
            if (status == 0) {
                UserDetails userDetails = User.withUsername(String.valueOf(patient.getPesel()))
                        .password(patient.getPassword()).roles("PATIENT").build();
                getInMemoryUserDetailsManager().createUser(userDetails);
                return ResponseCreator.jsonResponse("patient", patient.toJSONObject(),
                        "Successful adding patient. Pesel:" + patient.getPesel());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("Check patient data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding patient");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda obsługująca żądanie aktualizacji danych pacjenta pacjenta.
     *
     * @param patientData - tekst w formacie JSON zawierający informacje o pacjencie
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych pacjenta
     */
    @RequestMapping(value = "/api/updatePatient", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updatePatient(@RequestBody String patientData) {
        try {
            Patient patient = Patient.getInstance(patientData);
            int status = patientInterface.updatePatient(patient);
            if (status == 0) {
                UserDetails userDetails =
                        getInMemoryUserDetailsManager().loadUserByUsername(String.valueOf(patient.getPesel()));
                getInMemoryUserDetailsManager().updatePassword(userDetails, patient.getPassword());
                return ResponseCreator.jsonResponse(
                        "Successful updating patient with pesel = " + patient.getPesel());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No patient with pesel = " + patient.getPesel());
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating patient with pesel = " + patient.getPesel());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda obsługująca żądanie usuwania pacjenta.
     *
     * @param pesel - pesel pacjenta
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania pacjenta
     */
    @RequestMapping(value = "/api/deletePatient", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deletePatient(@RequestParam("pesel") long pesel) {
        int status = patientInterface.deletePatient(pesel);
        if (status == 0) {
            getInMemoryUserDetailsManager().deleteUser(String.valueOf(pesel));
            return ResponseCreator.jsonResponse("Successful deleting patient with pesel = " + pesel);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No patient with pesel = " + pesel);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting patient with pesel = " + pesel);
        }
    }

    private String createResponseWithPatient(Patient patient, long pesel) {
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
