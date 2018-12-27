package com.example.ProjektSZBD;

import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestControllers.HospitalRestController;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
public class ProjektSzbdApplicationTests {

    private HospitalRestController hospitalRestController;
    private HospitalInterface hospitalInterface;

    @Before
    public void setUp() {
        hospitalInterface = new HospitalInterface() {
            @Override
            public List<Hospital> getAllHospitals() {
                ArrayList<Hospital> hospitals = new ArrayList<>();
                hospitals.add(new Hospital(1, "Szpital im. Zbigniewa Religi",
                        "ul. Szpitalna 28", "Poznań"));
                return hospitals;
            }

            @Override
            public Hospital getHospitalById(int id) {
                return null;
            }
        };
        this.hospitalRestController = new HospitalRestController(hospitalInterface);
    }

    @Test
    public void contextLoads() {
        assert (true);
    }

    @Test
    public void testHospitalRestController() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject response = (JSONObject) parser.parse(hospitalRestController.getHospitals());
            assertEquals(response.get("resp_status"), "ok");
            assertEquals(response.get("description"), "List of all hospitals");
            JSONArray hospitals = (JSONArray) response.get("hospitals");
            assertEquals(hospitals.size(), 1);
            JSONObject hospital = (JSONObject) hospitals.get(0);
            assertEquals(hospital.get("id"), (long) 1);
            assertEquals(hospital.get("name"), "Szpital im. Zbigniewa Religi");
            assertEquals(hospital.get("address"), "ul. Szpitalna 28");
            assertEquals(hospital.get("city"), "Poznań");
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

}
