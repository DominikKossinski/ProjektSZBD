package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Salary;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.SalaryInterface;
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
 * RestController obsługujący żądania związane z płacami.
 */
@RestController
public class SalaryRestController {

    /**
     * Pole przechowujące interfejs wystawiający dane o płacach
     */
    private SalaryInterface salaryInterface;

    /**
     * Publiczny konstruktor klasy ustawiający domyślny interfejs wystawiający dane.
     */
    public SalaryRestController() {
        this.salaryInterface = new SalaryInterface() {
            @Override
            public Salary getSalaryByPosition(String position) {
                try {
                    return getJdbcTemplate().queryForObject("SELECT * FROM PLACE WHERE STANOWISKO = '" + position + "'",
                            (rs, arg1) -> new Salary(rs.getString("stanowisko"), rs.getFloat("placa_min"),
                                    rs.getFloat("placa_max")));
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }

            @Override
            public List<Salary> getAllSalaries() {
                return getJdbcTemplate().query("SELECT * FROM PLACE",
                        (rs, arg1) -> new Salary(rs.getString("stanowisko"), rs.getFloat("placa_min"),
                                rs.getFloat("placa_max")));
            }

            @Override
            public int insertSalary(Salary salary) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call insertSalary(?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.setString(2, salary.getPosition());
                    call.setDouble(3, salary.getMinSalary());
                    call.setDouble(4, salary.getMaxSalary());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int updateSalary(Salary salary) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call updateSalary(?, ?, ?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(5, OracleTypes.NUMBER);
                    call.setString(2, salary.getPosition());
                    call.setDouble(3, salary.getMinSalary());
                    call.setDouble(4, salary.getMaxSalary());
                    call.execute();
                    return call.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return -5;
                }
            }

            @Override
            public int deleteSalary(String position) {
                try {
                    CallableStatement call = getJdbcTemplate().getDataSource().getConnection().prepareCall(
                            "{? = call deleteSalary(?, ?)}"
                    );
                    call.registerOutParameter(1, OracleTypes.NUMBER);
                    call.registerOutParameter(3, OracleTypes.NUMBER);
                    call.setString(2, position);
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
     * Publiczny konstruktor ustawiający interfejs wystawiający dane przekazany przez parametr.
     *
     * @param salaryInterface - interfejs wystawiający dane
     */
    public SalaryRestController(SalaryInterface salaryInterface) {
        this.salaryInterface = salaryInterface;
    }


    /**
     * Metoda odpowiadająca za obsługę żądań dotyczących płacy dla danego stanowiska
     *
     * @param position - stanowisko
     * @return (String) - tekst w formacie JSON zawierający dane o płacach
     */
    @RequestMapping(value = "/api/salary", method = RequestMethod.GET)
    public String getSalary(@RequestParam(value = "position", defaultValue = "", required = false) String position) {
        if (position.isEmpty()) {
            List<Salary> salaries = salaryInterface.getAllSalaries();
            JSONArray salariesArray = new JSONArray();
            for (Salary salary : salaries) {
                try {
                    salariesArray.add(salary.toJSONObject());
                } catch (ParseException e) {
                    return ResponseCreator.parseErrorResponse(e);
                }
            }
            return ResponseCreator.jsonResponse("salaries", salariesArray, "List of all salaries");
        } else {
            Salary salary = salaryInterface.getSalaryByPosition(position);
            try {
                if (salary != null) {
                    JSONObject salaryObject = salary.toJSONObject();
                    return ResponseCreator.jsonResponse("salary", salaryObject, "Salary for position = " + position);
                } else {
                    return ResponseCreator.jsonErrorResponse("No salary for position = " + position);
                }
            } catch (ParseException e) {
                return ResponseCreator.parseErrorResponse(e);
            }
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań wstawiania płacy.
     *
     * @param salaryData - tekst w formacie JSON zawierający dane o płacy
     * @return (String) - odpowiedź serwera zawierająca status zakończenia dodawania płacy
     */
    @RequestMapping(value = "/api/addSalary", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String insertSalary(@RequestBody String salaryData) {
        try {
            Salary salary = Salary.getInstance(salaryData);
            int status = salaryInterface.insertSalary(salary);
            if (status > 0) {
                return ResponseCreator.jsonResponse("salary", salary.toJSONObject(),
                        "Successful adding salary. Position:" + salary.getPosition());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("Check salary data");
            } else {
                return ResponseCreator.jsonErrorResponse("Error by adding salary");
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za obsługę żądań aktualizacji danych płacy.
     *
     * @param salaryData - tekst w formacie JSON zawierający dane o płacy
     * @return (String) - odpowiedź serwera zawierająca status zakończenia aktualizowania danych płacy
     */
    @RequestMapping(value = "/api/updateSalary", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String updateSalary(@RequestBody String salaryData) {
        try {
            Salary salary = Salary.getInstance(salaryData);
            int status = salaryInterface.updateSalary(salary);

            if (status == 0) {
                //TODO wywołanie procedury do korekcji płac
                return ResponseCreator.jsonResponse("Successful updating salary with position = " + salary.getPosition());
            } else if (status == -4) {
                return ResponseCreator.jsonErrorResponse("No salary with position = " + salary.getPosition());
            } else if (status == -2) {
                return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
            } else {
                return ResponseCreator.jsonErrorResponse(
                        "Error by updating salary with position = " + salary.getPosition());
            }
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    /**
     * Metoda odpowiadająca za żądania usunięcia płacy.
     *
     * @param position - nazwa stanowiska
     * @return (String) - odpowiedź serwera zawierająca status zakończenia usuwania płacy
     */
    @RequestMapping(value = "/api/deleteSalary", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String deleteSalary(@RequestParam("position") String position) {
        int status = salaryInterface.deleteSalary(position);
        if (status == 0) {
            return ResponseCreator.jsonResponse("Successful deleting salary with position = " + position);
        } else if (status == -4) {
            return ResponseCreator.jsonErrorResponse("No salary with position = " + position);
        } else if (status == -2) {
            return ResponseCreator.jsonErrorResponse("SQL Integrity Constraint Violation Exception");
        } else {
            return ResponseCreator.jsonErrorResponse("Error by deleting salary with position = " + position);
        }
    }
}
