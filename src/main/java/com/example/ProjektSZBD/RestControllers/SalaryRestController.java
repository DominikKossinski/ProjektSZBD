package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Salary;
import com.example.ProjektSZBD.ResponseCreator;
import com.example.ProjektSZBD.RestInterfaces.SalaryInterface;
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
    @RequestMapping("/api/salary")
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
}
