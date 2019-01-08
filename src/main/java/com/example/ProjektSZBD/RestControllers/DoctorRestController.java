package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import oracle.jdbc.OracleTypes;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
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
 * RestController odpowiadający za obsługę żądań związanych danymi lekarzy.
 */
@RestController
public class DoctorRestController {

    /**
     * Interfejs odpowiadający za wystawianie danych o lekarzach.
     */
    private DoctorInterface doctorInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs do pobierania danych z bazy danych.
     */
    public DoctorRestController() {
        this.doctorInterface = new DoctorInterface() {
            @Override
            public List<Doctor> getAllDoctors() {
                return getJdbcTemplate().query("SELECT * FROM LEKARZE",
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getDouble("placa"),
                                rs.getLong("id_oddzialu"), rs.getString("stanowisko"),
                                ""));
            }

            @Override
            public List<Doctor> getDoctorsByHospitalId(long hospitalId) {
                return getJdbcTemplate().query("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.PLACA, l.ID_ODDZIALU, " +
                                "l.stanowisko, l.HASLO " +
                                "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where  o.ID_SZPITALA = " + hospitalId,
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getDouble("placa"),
                                rs.getLong("id_oddzialu"), rs.getString("stanowisko"),
                                ""));
            }

            @Override
            public List<Doctor> getDoctorsByHospitalSectionId(long hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM LEKARZE WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getDouble("placa"),
                                rs.getLong("id_oddzialu"), rs.getString("stanowisko"),
                                ""));
            }

            @Override
            public List<Doctor> getAllDoctorsInfo() {
                return getJdbcTemplate().query("SELECT id_lekarza, imie, nazwisko, id_oddzialu, stanowisko FROM lekarze",
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getLong("id_oddzialu"),
                                rs.getString("stanowisko")));
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalId(long hospitalId) {
                return getJdbcTemplate().query("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.ID_ODDZIALU, " +
                                "l.stanowisko " +
                                "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where  o.ID_SZPITALA = " + hospitalId,
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getLong("id_oddzialu"),
                                rs.getString("stanowisko")));
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalSectionId(long hospitalSectionId) {
                return getJdbcTemplate().query("SELECT id_lekarza, imie, nazwisko, id_oddzialu, stanowisko " +
                                "FROM lekarze WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Doctor(rs.getLong("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getLong("id_oddzialu"),
                                rs.getString("stanowisko")));
            }

            @Override
            public long insertDoctor(Doctor doctor) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertdoctor(?, ?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(8, OracleTypes.NUMBER);
                    call.setString(2, doctor.getFirstName());
                    call.setString(3, doctor.getLastName());
                    call.setDouble(4, doctor.getSalary());
                    call.setLong(5, doctor.getHospitalSectionId());
                    call.setString(6, doctor.getPosition());
                    call.setString(7, doctor.getPassword());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            public int deleteDoctor(long id) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteDoctor(?, ?)}"
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
            public int updateDoctor(Doctor doctor) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateDoctor(?, ?, ?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(9, OracleTypes.NUMBER);
                    call.setLong(2, doctor.getId());
                    call.setString(3, doctor.getFirstName());
                    call.setString(4, doctor.getLastName());
                    call.setDouble(5, doctor.getSalary());
                    call.setLong(6, doctor.getHospitalSectionId());
                    call.setString(7, doctor.getPosition());
                    call.setString(8, doctor.getPassword());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateDoctorNoPassword(Doctor doctor) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateDoctorNoPassword(?, ?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(8, OracleTypes.NUMBER);
                    call.setLong(2, doctor.getId());
                    call.setString(3, doctor.getFirstName());
                    call.setString(4, doctor.getLastName());
                    call.setDouble(5, doctor.getSalary());
                    call.setLong(6, doctor.getHospitalSectionId());
                    call.setString(7, doctor.getPosition());
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
     * Publiczny konstruktor klasy ustawiajcy interfejs na interfejs przekazany jako argument.
     *
     * @param doctorInterface - interfejs wystawiający dane o lekarzach
     */
    public DoctorRestController(DoctorInterface doctorInterface) {
        this.doctorInterface = doctorInterface;
    }

    /**
     * Metoda klasy odpowiadająca za obsługę żądania pełnych danych lekarzy, z możliwością
     * obsługi żądania dotyczącego wszystkich lekarzy (gdy hospitalId oraz hospitalSectionId nie
     * zostało podane), lekarzy z danego szpitala (gdy hospitalId zostało podane) i lekarzy
     * z danego oddziału (gdy hospitalSectionId zostało podane).
     *
     * @param hospitalId        - id szpitala (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param hospitalSectionId - id oddziału (jeśli nie zostało podane to przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON zawierający pełne dane wszystkich lekarzy
     */
    @RequestMapping(value = "/api/doctors", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAllDoctors(
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) long hospitalSectionId,
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) long hospitalId) {
        List<Doctor> doctors;
        if (hospitalId == -1 && hospitalSectionId == -1) {
            doctors = doctorInterface.getAllDoctors();
            return createResponseWithDoctorsArray(doctors, "List of all doctors", false);
        } else if (hospitalSectionId != -1) {
            doctors = doctorInterface.getDoctorsByHospitalSectionId(hospitalSectionId);
            return createResponseWithDoctorsArray(doctors,
                    "List of doctors from hospital section with id = " + hospitalSectionId, false);
        } else {
            doctors = doctorInterface.getDoctorsByHospitalId(hospitalId);
            return createResponseWithDoctorsArray(doctors,
                    "List of doctors from hospital with id = " + hospitalId, false);
        }

    }

    /**
     * Metoda klasy odpowiadająca za obsługę żądania podstawowych danych lekarzy, z możliwością
     * obsługi żądania dotyczącego wszystkich lekarzy (gdy hospitalId oraz hospitalSectionId nie
     * zostało podane), lekarzy z danego szpitala (gdy hospitalId zostało podane) i lekarzy
     * z danego oddziału (gdy hospitalSectionId zostało podane).
     *
     * @param hospitalId        - id szpitala (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param hospitalSectionId - id oddziału (jeśli nie zostało podane to przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON zawierający podstawowe dane wszystkich lekarzy
     */
    @RequestMapping(value = "/api/doctorsInfo", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAllDoctorsInfo(
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) long hospitalSectionId,
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) long hospitalId) {
        List<Doctor> doctors;
        if (hospitalId == -1 && hospitalSectionId == -1) {
            doctors = doctorInterface.getAllDoctorsInfo();
            return createResponseWithDoctorsArray(doctors, "List of all doctors info", true);
        } else if (hospitalSectionId != -1) {
            doctors = doctorInterface.getDoctorsInfoByHospitalSectionId(hospitalSectionId);
            return createResponseWithDoctorsArray(doctors,
                    "List of doctors info from hospital section with id = " + hospitalSectionId, true);
        } else {
            doctors = doctorInterface.getDoctorsInfoByHospitalId(hospitalId);
            return createResponseWithDoctorsArray(doctors,
                    "List of doctors info from hospital with id = " + hospitalId, true);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania dodania nowego lekarza.
     *
     * @param doctorData - tekst w formacie JSON zawierający dane o lekarzu.
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania lekarza
     */
    @RequestMapping(value = "/api/addDoctor", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addNewDoctor(@RequestBody String doctorData) {
        try {
            Doctor doctor = Doctor.getInstance(doctorData);
            long id = doctorInterface.insertDoctor(doctor);
            if (id > 0) {
                doctor.setId(id);
                UserDetails userDetails = User.withUsername(String.valueOf(doctor.getId()))
                        .password(doctor.getPassword()).roles(doctor.getPosition()).build();
                getInMemoryUserDetailsManager().createUser(userDetails);
                return ResponseCreator.jsonResponse("doctor", doctor.toJSONObject(),
                        "Successful adding doctor. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check doctor data");
            } else if (id == -3) {
                return ResponseCreator.jsonErrorResponse(
                        "No hospitalSection with id = " + doctor.getHospitalSectionId() +
                                "\n or no position with name = " + doctor.getPosition());
            } else {
                return ResponseCreator.jsonErrorResponse("Hospital id");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądania związanego z usuwaniem lekarza.
     *
     * @param id - id lekarza
     * @return (String) - odpowiedź serwera zawierającą status zakończenia opracji usuwania
     */
    @RequestMapping(value = "/api/deleteDoctor", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteDoctor(@RequestParam("id") long id) {
        int status = doctorInterface.deleteDoctor(id);
        if (status == 0) {
            getInMemoryUserDetailsManager().deleteUser(String.valueOf(id));
            return ResponseCreator.jsonResponse("Successful deleting doctor with id = " + id);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No doctor with id = " + id);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting doctor with id = " + id);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizacji podstawowych danych lekarza.
     *
     * @param doctorData - tekst w formacie JSON zawierający podstawowe dane o lekarza
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania podstawowych danych lekarza
     */
    @RequestMapping(value = "/api/updateDoctor", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateDoctorNoPassword(@RequestBody String doctorData) {
        try {
            Doctor doctor = Doctor.getInstance(doctorData);
            int status = doctorInterface.updateDoctorNoPassword(doctor);
            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating doctor with id = " + doctor.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No doctor with id = " + doctor.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating doctor with id = " + doctor.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizacji danych lekarza.
     *
     * @param doctorData - tekst w formacie JSON zawierający pełne dane o lekarza
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania pełnych danych lekarza
     */
    @RequestMapping(value = "/api/admin/updateDoctor", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateDoctor(@RequestBody String doctorData) {
        try {
            Doctor doctor = Doctor.getInstance(doctorData);
            int status = doctorInterface.updateDoctor(doctor);
            if (status == 0) {
                UserDetails userDetails =
                        getInMemoryUserDetailsManager().loadUserByUsername(String.valueOf(doctor.getId()));
                getInMemoryUserDetailsManager().updatePassword(userDetails, doctor.getPassword());
                return ResponseCreator.jsonResponse("ADMIN: Successful updating doctor with id = " + doctor.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("ADMIN: No doctor with id = " + doctor.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "ADMIN: Error by updating doctor with id = " + doctor.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }


    /**
     * Metoda służąca do zwracania odpowiedzi serwera.
     *
     * @param doctors     - lista doktorów
     * @param description - opis odpowiedzi
     * @return (String) - tekst zawierający odpowiedź serwera.
     */
    private String createResponseWithDoctorsArray(List<Doctor> doctors, String description, boolean info) {
        JSONArray doctorsArray = new JSONArray();
        for (Doctor doctor : doctors) {
            try {
                if (info) {
                    doctorsArray.add(doctor.toSimpleJSONObject());
                } else {
                    doctorsArray.add(doctor.toJSONObject());
                }
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("doctors", doctorsArray, description);
    }
}
