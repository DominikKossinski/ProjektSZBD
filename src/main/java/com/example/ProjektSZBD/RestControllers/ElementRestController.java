package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Element;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.ElementInterface;
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
 * RestController odpowiadający za żądania dotyczące elementów wyposażenia.
 */
@RestController
public class ElementRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane o elementach wyposażenia.
     */
    private ElementInterface elementInterface;

    /**
     * Publiczny konstruktor ustawiający domyślny interfejs do pobierania danych z bazy danych.
     */
    public ElementRestController() {
        elementInterface = new ElementInterface() {
            @Override
            public Element getElementById(int id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM ELEMENTY_WYPOSAZENIA WHERE ID_ELEMENTU = " + id,
                            (rs, arg1) -> new Element(rs.getInt("id_elementu"), rs.getString("nazwa"),
                                    rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                    rs.getInt("id_oddzialu")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Element> getAllElements() {
                return getJdbcTemplate().query("SELECT * FROM ELEMENTY_WYPOSAZENIA",
                        (rs, arg1) -> new Element(rs.getInt("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getInt("id_oddzialu")));
            }

            @Override
            public List<Element> getElementsByHospitalSectionId(int hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM ELEMENTY_WYPOSAZENIA WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Element(rs.getInt("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getInt("id_oddzialu")));
            }

            @Override
            public List<Element> getElementsByHospitalId(int hospitalId) {
                return getJdbcTemplate().query("SELECT e.id_elementu, e.nazwa, e.ilosc, e.cena_jednostkowa, " +
                                "e.id_oddzialu FROM ELEMENTY_WYPOSAZENIA e " +
                                "JOIN ODDZIALY o on e.id_oddzialu = o.id_oddzialu " +
                                "JOIN SZPITALE s on o.id_szpitala = o.id_szpitala " +
                                "WHERE s.id_szpitala = " + hospitalId,
                        (rs, arg1) -> new Element(rs.getInt("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getInt("id_oddzialu")));
            }
        };
    }

    /**
     * Publiczny konstruktor ustawiający interfejs na interfejs przekazany jako argument.
     *
     * @param elementInterface
     */
    public ElementRestController(ElementInterface elementInterface) {
        this.elementInterface = elementInterface;
    }

    /**
     * Metoda obsługująca żądania dotyczące informacji o elementach wyposażenia. W przypadku
     * gdy został podany parametr:
     * - id - zwraca informacje o obiekcie z danym id,
     * - hospitalSectionId - zwraca informacje o elementach wyposażenia z oddziału z podanym hospitalSectionId
     * - hospitalId - zwraca informacje o elementach wyposażenia w szpitalu z podanym hospitalId
     * - jeśli żaden paramatr nie zostanie podany to zwraca listę wszystkich elementów.
     *
     * @param hospitalId        - id szpitala (domyślna wartość -1)
     * @param hospitalSectionId - id oddzialu (domyślna wartość -1)
     * @param id                - id elementu (domyślna wartość -1)
     * @return (String) - tekst w formacie JSON zawierający dane o żądanych elementach wyposażenia
     */
    @RequestMapping("/api/elements")
    public String getElements(
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) int hospitalId,
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) int hospitalSectionId,
            @RequestParam(name = "id", defaultValue = "-1", required = false) int id
    ) {
        if (hospitalId == -1 && hospitalSectionId == -1 && id == -1) {
            List<Element> elements = elementInterface.getAllElements();
            JSONArray elementsArray = new JSONArray();
            for (Element element : elements) {
                try {
                    elementsArray.add(element.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("elements", elementsArray, "List of all elements");
        }
        if (id != -1) {
            Element element = elementInterface.getElementById(id);
            try {
                if (element != null) {
                    JSONObject elementObject = element.toJSONObject();
                    return ResponseCreator.jsonResponse("element", elementObject, "Element with id = " + id);
                } else {
                    return ResponseCreator.jsonErrorResponse("No element with id = " + id);
                }
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        } else if (hospitalSectionId != -1) {
            List<Element> elements = elementInterface.getElementsByHospitalSectionId(hospitalSectionId);
            JSONArray elementsArray = new JSONArray();
            for (Element element : elements) {
                try {
                    elementsArray.add(element.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("elements", elementsArray,
                    "List of elements from section with id = " + hospitalSectionId);
        } else if (hospitalId != -1) {
            List<Element> elements = elementInterface.getElementsByHospitalId(hospitalId);
            JSONArray elementsArray = new JSONArray();
            for (Element element : elements) {
                try {
                    elementsArray.add(element.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("elements", elementsArray,
                    "List of elements from hospital with id =" + hospitalId);
        }

        return ResponseCreator.jsonErrorResponse("You can use only one parameter");
    }
}
