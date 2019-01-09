package com.example.ProjektSZBD.SalaryTests;

import com.example.ProjektSZBD.Data.Salary;
import com.example.ProjektSZBD.RestControllers.SalaryRestController;
import com.example.ProjektSZBD.RestInterfaces.SalaryInterface;
import com.example.ProjektSZBD.TestConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
public class SalaryTests {

    private SalaryDB salaryDB;
    private SalaryRestController restController;
    private JSONParser parser;

    @Before
    public void setUp() {
        parser = new JSONParser();
        salaryDB = new SalaryDB();
        SalaryInterface salaryInterface = new SalaryInterface() {
            @Override
            public Salary getSalaryByPosition(String position) {
                if (position.compareTo("Mechanik") == 0) {
                    return null;
                }
                return salaryDB.getSalaryByPosition(position);
            }

            @Override
            public List<Salary> getAllSalaries() {
                return salaryDB.getAllSalaries();
            }

            @Override
            public int insertSalary(Salary salary) {
                return 0;
            }

            @Override
            public int updateSalary(Salary salary) {
                return 0;
            }

            @Override
            public int deleteSalary(String position) {
                return 0;
            }
        };
        restController = new SalaryRestController(salaryInterface);
    }

    @Test
    public void getSalaryByPositionTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getSalary("Lekarz"));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Salary for position = Lekarz", response.get("description"));
            JSONObject salary = (JSONObject) response.get("salary");
            assertEquals("Lekarz", salary.get("position"));
            assertEquals(5000.0, salary.get("min_salary"));
            assertEquals(10000.0, salary.get("max_salary"));

            response = (JSONObject) parser.parse(restController.getSalary("Mechanik"));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No salary for position = Mechanik", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getAllSalaries() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getSalary(""));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all salaries", response.get("description"));
            JSONArray salaries = (JSONArray) response.get("salaries");
            assertEquals(2, salaries.size());
            JSONObject salary = (JSONObject) salaries.get(0);
            assertEquals("Lekarz", salary.get("position"));
            assertEquals(5000.0, salary.get("min_salary"));
            assertEquals(10000.0, salary.get("max_salary"));
            salary = (JSONObject) salaries.get(1);
            assertEquals("Ordynator", salary.get("position"));
            assertEquals(7500.0, salary.get("min_salary"));
            assertEquals(10000.0, salary.get("max_salary"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }
}
