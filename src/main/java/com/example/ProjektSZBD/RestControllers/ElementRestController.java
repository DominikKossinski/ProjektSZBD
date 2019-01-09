package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Element;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.ElementInterface;
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
            public Element getElementById(long id) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM ELEMENTY_WYPOSAZENIA WHERE ID_ELEMENTU = " + id,
                            (rs, arg1) -> new Element(rs.getLong("id_elementu"), rs.getString("nazwa"),
                                    rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                    rs.getLong("id_oddzialu")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Element> getAllElements() {
                return getJdbcTemplate().query("SELECT * FROM ELEMENTY_WYPOSAZENIA",
                        (rs, arg1) -> new Element(rs.getLong("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getLong("id_oddzialu")));
            }

            @Override
            public List<Element> getElementsByHospitalSectionId(long hospitalSectionId) {
                return getJdbcTemplate().query("SELECT * FROM ELEMENTY_WYPOSAZENIA WHERE ID_ODDZIALU = " + hospitalSectionId,
                        (rs, arg1) -> new Element(rs.getLong("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getLong("id_oddzialu")));
            }

            @Override
            public List<Element> getElementsByHospitalId(long hospitalId) {
                return getJdbcTemplate().query("SELECT e.id_elementu, e.nazwa, e.ilosc, e.cena_jednostkowa, " +
                                "e.id_oddzialu FROM ELEMENTY_WYPOSAZENIA e " +
                                "JOIN ODDZIALY o on e.id_oddzialu = o.id_oddzialu " +
                                "JOIN SZPITALE s on o.id_szpitala = o.id_szpitala " +
                                "WHERE s.id_szpitala = " + hospitalId,
                        (rs, arg1) -> new Element(rs.getLong("id_elementu"), rs.getString("nazwa"),
                                rs.getInt("ilosc"), rs.getFloat("cena_jednostkowa"),
                                rs.getLong("id_oddzialu")));
            }

            @Override
            public long insertElement(Element element) {
                CallableStatement call = null;
                try {
                    call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertElement(?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(6, OracleTypes.NUMBER);
                    call.setString(2, element.getName());
                    call.setInt(3, element.getCount());
                    call.setDouble(4, element.getPrice());
                    call.setLong(5, element.getHospitalSectionId());
                    call.execute();
                    return call.getLong(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -1;
                }
            }

            @Override
            public int deleteElement(long id) {
                int rowCount = getJdbcTemplate().update(
                        "DELETE FROM ELEMENTY_WYPOSAZENIA WHERE ID_ELEMENTU  = " + id);
                if (rowCount == 1) {
                    return 0;
                } else if (rowCount == 0) {
                    return -1;
                } else {
                    return -2;
                }
            }

            @Override
            public int updateElement(Element element) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateElement(?, ?, ?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(7, OracleTypes.NUMBER);
                    call.setLong(2, element.getId());
                    call.setString(3, element.getName());
                    call.setInt(4, element.getCount());
                    call.setDouble(5, element.getPrice());
                    call.setLong(6, element.getHospitalSectionId());
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
     * Publiczny konstruktor ustawiający interfejs na interfejs przekazany jako argument.
     *
     * @param elementInterface - interfejs wystawiający dane
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
            @RequestParam(name = "hospitalId", defaultValue = "-1", required = false) long hospitalId,
            @RequestParam(name = "hospitalSectionId", defaultValue = "-1", required = false) long hospitalSectionId,
            @RequestParam(name = "id", defaultValue = "-1", required = false) long id
    ) {
        if (hospitalId == -1 && hospitalSectionId == -1 && id == -1) {
            List<Element> elements = elementInterface.getAllElements();
            return createResponseWithElementsList(elements, "List of all elements");
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
            return createResponseWithElementsList(elements,
                    "List of elements from section with id = " + hospitalSectionId);
        } else if (hospitalId != -1) {
            List<Element> elements = elementInterface.getElementsByHospitalId(hospitalId);
            return createResponseWithElementsList(elements,
                    "List of elements from hospital with id = " + hospitalId);
        }

        return ResponseCreator.jsonErrorResponse("You can use only one parameter");
    }

    /**
     * Metoda obsługująca żądania wstawiania elementu wyposażenia
     *
     * @param elementData - tekst w formacie JSON zawierający dane o elemencie wyposażenia
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania elementu
     */
    @RequestMapping(value = "/api/addElement", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addElement(@RequestBody String elementData) {
        try {
            Element element = Element.getInstance(elementData);
            long id = elementInterface.insertElement(element);
            if (id > 0) {
                element.setId(id);
                return ResponseCreator.jsonResponse("element", element.toJSONObject(),
                        "Successful adding element. Id:" + id);
            } else if (id == -2) {
                return ResponseCreator.jsonErrorResponse("Check element data");
            } else if (id == -3) {
                return ResponseCreator.jsonErrorResponse(
                        "No hospitalSection with id = " + element.getHospitalSectionId());
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding element");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizowania danych elementu wyposażenia.
     *
     * @param elementData - tekst w formacie JSON zawierający dane o elemencie wyposażenia
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania elementu
     */
    @RequestMapping(value = "/api/updateElement", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateElement(@RequestBody String elementData) {
        try {
            Element element = Element.getInstance(elementData);
            int status = elementInterface.updateElement(element);
            if (status == 0) {
                return ResponseCreator.jsonResponse("Successful updating element with id = " + element.getId());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No element with id = " + element.getId());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by updating element with id = " + element.getId());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań dotyczących usuwania elementu wyposażenia
     *
     * @param id - id elementu
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania elementu
     */
    @RequestMapping(value = "/api/deleteElement", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteElement(@RequestParam("id") long id) {
        int status = elementInterface.deleteElement(id);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting element with id = " + id);
        } else if (status == -1) {
            return ResponseCreator.jsonErrorResponse("No element with id = " + id);
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting element with id = " + id);
        }
    }

    /**
     * Metoda do generowania odpowiedzi serwera.
     *
     * @param elements    - lista elementów wyposażenia
     * @param description - opis zawartości listy
     * @return (String) -  tekst zawierający odpowiedź serwera w formacie JSON
     */
    private String createResponseWithElementsList(List<Element> elements, String description) {
        JSONArray elementsArray = new JSONArray();
        for (Element element : elements) {
            try {
                elementsArray.add(element.toJSONObject());
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
        return ResponseCreator.jsonResponse("elements", elementsArray, description);
    }
}
