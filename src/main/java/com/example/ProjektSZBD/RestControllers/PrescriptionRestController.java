package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Prescription;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.PrescriptionInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController odpowiadający za obsługę żądań związanych z receptami.
 */
@RestController
public class PrescriptionRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane.
     */
    private PrescriptionInterface prescriptionInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public PrescriptionRestController() {
        this.prescriptionInterface = new PrescriptionInterface() {
            @Override
            public List<Prescription> getPrescriptionsByPesel(long pesel) {
                return getJdbcTemplate().query("select r.id_recepty, r.data_wystawienia, r.dawkowanie, " +
                                "r.id_choroby, r.id_pobytu" +
                                " from RECEPTY r join POBYTY p on r.ID_POBYTU = p.POBYTY_ID " +
                                "join pacjenci pa on p.pesel = pa.pesel where pa.pesel = " + pesel,
                        (rs, arg1) -> new Prescription(rs.getLong("id_recepty"),
                                rs.getDate("data_wystawienia"), rs.getString("dawkowanie"),
                                rs.getLong("id_choroby"), rs.getLong("id_pobytu")));
            }

            @Override
            public List<Prescription> getPrescriptionsByStayId(long stayId) {
                return getJdbcTemplate().query("select r.id_recepty, r.data_wystawienia, r.dawkowanie, " +
                                "r.id_choroby, r.id_pobytu" +
                                " from RECEPTY r join POBYTY p on r.ID_POBYTU = p.POBYTY_ID where p.pobyty_id = " + stayId,
                        (rs, arg1) -> new Prescription(rs.getLong("id_recepty"),
                                rs.getDate("data_wystawienia"), rs.getString("dawkowanie"),
                                rs.getLong("id_choroby"), rs.getLong("id_pobytu")));
            }

            @Override
            public List<Prescription> getPrescriptionsByDoctorId(long doctorId) {
                return getJdbcTemplate().query("select r.id_recepty, r.data_wystawienia, r.dawkowanie, " +
                                "r.id_choroby, r.id_pobytu" +
                                " from RECEPTY r join POBYTY p on r.ID_POBYTU = p.POBYTY_ID " +
                                "join lekarze l on l.id_lekarza = p.id_lekarza " +
                                "where l.id_lekarza = " + doctorId,
                        (rs, arg1) -> new Prescription(rs.getLong("id_recepty"),
                                rs.getDate("data_wystawienia"), rs.getString("dawkowanie"),
                                rs.getLong("id_choroby"), rs.getLong("id_pobytu")));
            }

            @Override
            public List<Prescription> getPrescriptionsByDoctorId(long doctorId, long pesel) {
                return getJdbcTemplate().query("select r.id_recepty, r.data_wystawienia, r.dawkowanie, " +
                                "r.id_choroby, r.id_pobytu" +
                                " from RECEPTY r join POBYTY p on r.ID_POBYTU = p.POBYTY_ID " +
                                "join pacjenci pa on p.pesel = pa.pesel " +
                                "join lekarze l on l.id_lekarza = p.id_lekarza " +
                                "where pa.pesel = " + pesel + ", l.id_lekarza = " + doctorId,
                        (rs, arg1) -> new Prescription(rs.getLong("id_recepty"),
                                rs.getDate("data_wystawienia"), rs.getString("dawkowanie"),
                                rs.getLong("id_choroby"), rs.getLong("id_pobytu")));
            }

            @Override
            public Prescription getPrescriptionById(long id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM RECEPTY WHERE ID_RECEPTY = " + id,
                            (rs, arg1) -> new Prescription(rs.getLong("id_recepty"), rs.getDate("data_wystawienia"),
                                    rs.getString("dawkowanie"), rs.getLong("id_choroby"),
                                    rs.getLong("id_pobytu")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }
        };
    }

    /**
     * Publiczny konstruktor klasy ustawiający interfejs na interfejs przekazany jako parametr.
     *
     * @param prescriptionInterface - interfejs wystawiający dane
     */
    public PrescriptionRestController(PrescriptionInterface prescriptionInterface) {
        this.prescriptionInterface = prescriptionInterface;
    }

    /**
     * Metoda zwracająca informacje o receptach. Przekazywane parametry wskazują na sposób wyszukiwania informacji.
     *
     * @param pesel    - pesel pacjenta
     * @param stayId   - id pobytu
     * @param doctorId - id lekarza
     * @param id       - id recepty
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych receptach
     */
    @RequestMapping("/api/prescriptions")
    public String getPrescriptions(
            @RequestParam(value = "pesel", defaultValue = "-1", required = false) long pesel,
            @RequestParam(value = "stayId", defaultValue = "-1", required = false) long stayId,
            @RequestParam(value = "doctorId", defaultValue = "-1", required = false) long doctorId,
            @RequestParam(value = "id", defaultValue = "-1", required = false) long id
    ) {
        if (id != -1) {
            Prescription prescription = prescriptionInterface.getPrescriptionById(id);
            if (prescription != null) {
                try {
                    JSONObject prescriptionObject = prescription.toJSONObject();
                    return ResponseCreator.jsonResponse("prescription", prescriptionObject,
                            "Prescription with id = " + id);
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            } else {
                return ResponseCreator.jsonErrorResponse("No prescription with id = " + id);
            }
        }
        if (pesel != -1) {
            List<Prescription> prescriptions = prescriptionInterface.getPrescriptionsByPesel(pesel);
            return createResponseWithPrescriptionsList(prescriptions,
                    "Prescriptions of patient with pesel = " + pesel);
        }
        if (stayId != -1) {
            List<Prescription> prescriptions = prescriptionInterface.getPrescriptionsByStayId(stayId);
            return createResponseWithPrescriptionsList(prescriptions,
                    "Prescriptions of stay with id = " + stayId);
        }
        if (doctorId != -1) {
            List<Prescription> prescriptions = prescriptionInterface.getPrescriptionsByDoctorId(doctorId);
            return createResponseWithPrescriptionsList(prescriptions,
                    "Prescriptions of doctor with id = " + doctorId);

        }
        return ResponseCreator.jsonErrorResponse(
                "You need to specify one of parameters [id, pesel, stayId, doctorId]");
    }

    @RequestMapping("/api/{pesel}/prescriptions")
    public String getPrescriptions(
            @PathVariable(name = "pesel") long pesel,
            @RequestParam(name = "doctorId", defaultValue = "-1", required = false) long doctorId
    ) {
        if (doctorId == -1) {
            List<Prescription> prescriptions = prescriptionInterface.getPrescriptionsByPesel(pesel);
            return createResponseWithPrescriptionsList(prescriptions,
                    "Prescriptions of patient with pesel = " + pesel);
        } else {
            List<Prescription> prescriptions = prescriptionInterface.getPrescriptionsByDoctorId(doctorId, pesel);
            return createResponseWithPrescriptionsList(prescriptions,
                    "Prescriptions of patient with pesel = " + pesel + " and with doctorId = " + doctorId);
        }
    }

    /**
     * Metoda zwracająca odpowiedź serwera na podstawie listy recept i jej opisu.
     *
     * @param prescriptions - lista recept
     * @param description   - opis
     * @return (String) - tekst zawierający odpowiedź serwera w formacie JSON
     */
    private String createResponseWithPrescriptionsList(List<Prescription> prescriptions, String description) {
        JSONArray prescriptionArray = new JSONArray();
        for (Prescription prescription : prescriptions) {
            try {
                prescriptionArray.add(prescription.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("prescriptions", prescriptionArray,
                description);
    }
}
