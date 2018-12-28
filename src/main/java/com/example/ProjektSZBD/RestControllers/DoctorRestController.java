package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getFloat("placa"),
                                rs.getInt("id_oddzialu"), rs.getString("stanowisko"),
                                rs.getString("haslo")));
            }

            @Override
            public List<Doctor> getDoctorsByHospitalId(int hospitalId) {
                return getJdbcTemplate().query("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.PLACA, l.ID_ODDZIALU, " +
                                "l.stanowisko, l.HASLO " +
                                "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where  o.ID_SZPITALA = " + hospitalId,
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getFloat("placa"),
                                rs.getInt("id_oddzialu"), rs.getString("stanowisko"),
                                rs.getString("haslo")));
            }

            @Override
            public List<Doctor> getDoctorsByHospitalSectionId(int hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM LEKARZE WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getFloat("placa"),
                                rs.getInt("id_oddzialu"), rs.getString("stanowisko"),
                                rs.getString("haslo")));
            }

            @Override
            public List<Doctor> getAllDoctorsInfo() {
                return getJdbcTemplate().query("SELECT id_lekarza, imie, nazwisko, id_oddzialu, stanowisko FROM lekarze",
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getInt("id_oddzialu"),
                                rs.getString("stanowisko")));
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalId(int hospitalId) {
                return getJdbcTemplate().query("select l.ID_LEKARZA, l.IMIE, l.NAZWISKO, l.ID_ODDZIALU, " +
                                "l.stanowisko " +
                                "from lekarze l join oddzialy o on l.id_oddzialu = o.id_oddzialu " +
                                "join SZPITALE s on s.ID_SZPITALA = o.ID_SZPITALA " +
                                "where  o.ID_SZPITALA = " + hospitalId,
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getInt("id_oddzialu"),
                                rs.getString("stanowisko")));
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalSectionId(int hospitalSectionId) {
                return getJdbcTemplate().query("SELECT id_lekarza, imie, nazwisko, id_oddzialu, stanowisko " +
                                "FROM lekarze WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Doctor(rs.getInt("id_lekarza"), rs.getString("imie"),
                                rs.getString("nazwisko"), rs.getInt("id_oddzialu"),
                                rs.getString("stanowisko")));
            }
        };
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
    @RequestMapping("/api/doctors")
    public String getAllDoctors(
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) int hospitalSectionId,
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) int hospitalId) {
        List<Doctor> doctors;
        if (hospitalId == -1 && hospitalSectionId == -1) {
            doctors = doctorInterface.getAllDoctors();
        } else if (hospitalSectionId != -1) {
            doctors = doctorInterface.getDoctorsByHospitalSectionId(hospitalSectionId);
        } else {
            doctors = doctorInterface.getDoctorsByHospitalId(hospitalId);
        }
        JSONArray doctorsArray = new JSONArray();
        for (Doctor doctor : doctors) {
            try {
                doctorsArray.add(doctor.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("doctors", doctorsArray, "List of all doctors");
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
    @RequestMapping("/api/doctorsInfo")
    public String getAllDoctorsInfo(
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) int hospitalSectionId,
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) int hospitalId) {
        List<Doctor> doctors;
        if (hospitalId == -1 && hospitalSectionId == -1) {
            doctors = doctorInterface.getAllDoctorsInfo();
        } else if (hospitalSectionId != -1) {
            doctors = doctorInterface.getDoctorsInfoByHospitalSectionId(hospitalSectionId);
        } else {
            doctors = doctorInterface.getDoctorsInfoByHospitalId(hospitalId);
        }
        JSONArray doctorsArray = new JSONArray();
        for (Doctor doctor : doctors) {
            try {
                doctorsArray.add(doctor.toSimpleJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("doctors", doctorsArray, "List of all doctors info");
    }

}
