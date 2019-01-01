package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Stay;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.StayInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

/**
 * RestController odpowiadający za obsługę żądań dotyczących pobytów.
 */
@RestController
public class StayRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane.
     */
    private StayInterface stayInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs pobierający dane z bazy danych.
     */
    public StayRestController() {
        this.stayInterface = new StayInterface() {
            @Override
            public Stay getStayById(long id) {
                return getJdbcTemplate().queryForObject("SELECT * from pobyty where pobyty_id = " + id,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }

            @Override
            public List<Stay> getStayByPesel(long pesel) {
                return getJdbcTemplate().query("SELECT * from pobyty where pesel = " + pesel,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }

            @Override
            public List<Stay> getStayByDoctor(long doctorId) {
                return getJdbcTemplate().query("SELECT * from pobyty where id_lekarza = " + doctorId,
                        (rs, arg1) -> new Stay(rs.getLong("id_pobytu"), rs.getDate("termin_przyjecia"),
                                rs.getDate("termin_wypisu"), rs.getLong("id_pokoju"),
                                rs.getLong("id_lekarza"), rs.getLong("pesel")));
            }
        };
    }

    /**
     * Publiczny konstruktor klasy ustawiający interfejs na interfejs przekazany przez parametr.
     *
     * @param stayInterface - interfejs wystawiający dane
     */
    public StayRestController(StayInterface stayInterface) {
        this.stayInterface = stayInterface;
    }

    /**
     * Metoda zwracająca informacje o pobytach. Jeśli id zostanie podane zwróci informacje o pobycie
     * z podanym id. Jeśli zostanie podany pesel zwróci informacje o pobytach pacjenta. Jeśli zostanie
     * podene id lekarza zostanie zwrócona lista pobytów które utworzył dany lekarz.
     *
     * @param id       - id pobytu (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param pesel    - pesel pacjenta (jeśli nie zostało podane to przyjmuje wartość -1)
     * @param doctorId - id lekarza (jeśli nie zostało podane to przyjmuje wartość -1)
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych pobytach
     */
    @RequestMapping("/api/stays")
    public String getStays(
            @RequestParam(value = "id", defaultValue = "-1", required = false) long id,
            @RequestParam(value = "pesel", defaultValue = "-1", required = false) long pesel,
            @RequestParam(value = "doctorId", defaultValue = "-1", required = false) long doctorId
    ) {
        if (id != -1) {
            Stay stay = stayInterface.getStayById(id);
            if (stay != null) {
                try {
                    JSONObject stayObject = stay.toJSONObject();
                    return ResponseCreator.jsonResponse("stay", stayObject, "Stay with id = " + id);
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }

            } else {
                return ResponseCreator.jsonErrorResponse("No stay with id = " + id);
            }
        }
        if (pesel != -1) {
            List<Stay> stays = stayInterface.getStayByPesel(pesel);
            return createResponseWithStaysList(stays, "Stays of patient with pesel = " + pesel);
        }
        if (doctorId != -1) {
            List<Stay> stays = stayInterface.getStayByDoctor(doctorId);
            return createResponseWithStaysList(stays, "Stays with doctorId = " + doctorId);
        }
        return ResponseCreator.jsonErrorResponse(
                "You need to specify one of parameters [id, pesel, doctorId]");
    }

    private String createResponseWithStaysList(List<Stay> stays, String description) {
        JSONArray staysArray = new JSONArray();
        for (Stay stay : stays) {
            try {
                staysArray.add(stay.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("stays", staysArray,
                description);
    }
}
