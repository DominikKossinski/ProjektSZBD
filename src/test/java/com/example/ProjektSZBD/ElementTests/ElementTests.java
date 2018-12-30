package com.example.ProjektSZBD.ElementTests;

import com.example.ProjektSZBD.Data.Element;
import com.example.ProjektSZBD.RestControllers.ElementRestController;
import com.example.ProjektSZBD.RestInterfaces.ElementInterface;
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
public class ElementTests {

    private ElementDB elementDB;
    private ElementRestController restController;
    private JSONParser parser;

    @Before
    public void setUp() {
        parser = new JSONParser();
        elementDB = new ElementDB();
        ElementInterface elementInterface = new ElementInterface() {
            @Override
            public Element getElementById(int id) {
                if (id == 0) {
                    return null;
                }
                return elementDB.getElementById(id);
            }

            @Override
            public List<Element> getAllElements() {
                return elementDB.getAllElements();
            }

            @Override
            public List<Element> getElementsByHospitalSectionId(int hospitalSectionId) {
                return elementDB.getElementsByHospitalSectionId(hospitalSectionId);
            }

            @Override
            public List<Element> getElementsByHospitalId(int hospitalId) {
                return elementDB.getAllElements();
            }
        };
        restController = new ElementRestController(elementInterface);
    }

    @Test
    public void getElementByIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getElements(-1, -1, 1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Element with id = 1", response.get("description"));
            JSONObject element = (JSONObject) response.get("element");
            assertEquals((long) 1, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 1, element.get("hospital_section_id"));

            response = (JSONObject) parser.parse(restController.getElements(-1, -1, 0));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No element with id = 0", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getAllElementsTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getElements(-1, -1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all elements", response.get("description"));
            JSONArray elements = (JSONArray) response.get("elements");
            assertEquals(2, elements.size());

            JSONObject element = (JSONObject) elements.get(0);
            assertEquals((long) 1, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 1, element.get("hospital_section_id"));

            element = (JSONObject) elements.get(1);
            assertEquals((long) 2, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 2, element.get("hospital_section_id"));

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    @Test
    public void getElementsByHospitalSectionIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getElements(-1, 1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of elements from section with id = 1", response.get("description"));
            JSONArray elements = (JSONArray) response.get("elements");
            assertEquals(2, elements.size());

            JSONObject element = (JSONObject) elements.get(0);
            assertEquals((long) 1, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 1, element.get("hospital_section_id"));

            element = (JSONObject) elements.get(1);
            assertEquals((long) 2, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 1, element.get("hospital_section_id"));

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    @Test
    public void getElementsByHospitalId() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getElements(1, -1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of elements from hospital with id = 1", response.get("description"));
            JSONArray elements = (JSONArray) response.get("elements");
            assertEquals(2, elements.size());

            JSONObject element = (JSONObject) elements.get(0);
            assertEquals((long) 1, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 1, element.get("hospital_section_id"));

            element = (JSONObject) elements.get(1);
            assertEquals((long) 2, element.get("id"));
            assertEquals("Skalpel", element.get("name"));
            assertEquals((long) 5, element.get("count"));
            assertEquals(5.5, element.get("price"));
            assertEquals((long) 2, element.get("hospital_section_id"));

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }
}
