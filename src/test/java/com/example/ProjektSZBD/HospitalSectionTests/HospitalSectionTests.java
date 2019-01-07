package com.example.ProjektSZBD.HospitalSectionTests;


import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.HospitalSection;
import com.example.ProjektSZBD.RestControllers.HospitalSectionRestController;
import com.example.ProjektSZBD.RestInterfaces.HospitalSectionInterface;
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
public class HospitalSectionTests {

    private HospitalSectionInterface hospitalSectionInterface;
    private HospitalSectionDB hospitalSectionDB;
    private HospitalSectionRestController restController;

    @Before
    public void setUp() {
        this.hospitalSectionDB = new HospitalSectionDB();
        this.hospitalSectionInterface = new HospitalSectionInterface() {
            @Override
            public List<HospitalSection> getHospitalSectionsByHospitalId(long id) {
                return hospitalSectionDB.getHospitalSectionByHospitalId(id);
            }

            @Override
            public Ordynator getHospitalSectionOrdynator(long hospitalSectionId) {
                if (hospitalSectionId == 0) {
                    return null;
                }
                return hospitalSectionDB.getHospitalSectionOrdynator(hospitalSectionId);
            }

            @Override
            public long insertHospitalSection(HospitalSection hospitalSection) {
                return 0;
            }

            @Override
            public int updateHospitalSection(HospitalSection hospitalSection) {
                return 0;
            }

            @Override
            public int deleteHospitalSection(long id) {
                return 0;
            }
        };
        this.restController = new HospitalSectionRestController(hospitalSectionInterface);
    }

    @Test
    public void getHospitalSectionsByHospitalIdTests() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitalSectionsByHospitalId(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of sections in hospital with id = 1", response.get("description"));
            JSONArray sections = (JSONArray) response.get("sections");
            assertEquals(2, sections.size());
            JSONObject section1 = (JSONObject) sections.get(0);
            assertEquals((long) 1, section1.get("id"));
            assertEquals("Kardiologia", section1.get("name"));
            assertEquals((long) 1, section1.get("hospital_id"));
            JSONObject section2 = (JSONObject) sections.get(1);
            assertEquals((long) 2, section2.get("id"));
            assertEquals("Chirurgia", section2.get("name"));
            assertEquals((long) 1, section2.get("hospital_id"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getSectionOrdynator() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getSectionOrdynator(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Ordynator of hospital section with id = 1", response.get("description"));
            JSONObject ordynator = (JSONObject) response.get("ordynator");
            assertEquals("Kardiologia", ordynator.get("hospital_section_name"));
            assertEquals((long) 1, ordynator.get("hospital_id"));
            JSONObject doctor = (JSONObject) ordynator.get("doctor");
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Ordynator", doctor.get("position"));

            response = (JSONObject) parser.parse(restController.getSectionOrdynator(0));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No ordynator of hospitalSection with id = 0", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }
}
