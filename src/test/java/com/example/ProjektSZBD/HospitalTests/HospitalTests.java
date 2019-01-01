package com.example.ProjektSZBD.HospitalTests;


import com.example.ProjektSZBD.Data.Doctors.Director;
import com.example.ProjektSZBD.Data.Doctors.Ordynator;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestControllers.HospitalRestController;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
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
public class HospitalTests {

    private HospitalInterface hospitalInterface;
    private HospitalDB hospitalDB;
    private HospitalRestController restController;
    private JSONParser parser;

    @Before
    public void setUp() {
        parser = new JSONParser();
        this.hospitalDB = new HospitalDB();
        this.hospitalInterface = new HospitalInterface() {
            @Override
            public List<Hospital> getAllHospitals() {
                return hospitalDB.getAllHospitals();
            }

            @Override
            public Hospital getHospitalById(long id) {
                if (id == 0) {
                    return null;
                }
                return hospitalDB.getHospitalById(id);
            }

            @Override
            public Director getHospitalDirector(long hospitalId) {
                if (hospitalId == 0) {
                    return null;
                }
                return hospitalDB.getHospitalDirector(hospitalId);
            }

            @Override
            public List<Director> getHospitalsDirectors() {
                return hospitalDB.getHospitalDirectors();
            }

            @Override
            public List<Ordynator> getHospitalOrdynators(long hospitalId) {
                return hospitalDB.getHospitalOrdynators(hospitalId);
            }
        };
        this.restController = new HospitalRestController(hospitalInterface);
    }

    @Test
    public void getAllHospitalsTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitals());
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all hospitals", response.get("description"));
            JSONArray hospitals = (JSONArray) response.get("hospitals");
            assertEquals(2, hospitals.size());
            JSONObject hospital = (JSONObject) hospitals.get(0);
            assertEquals((long) 1, hospital.get("id"));
            assertEquals("Szpital", hospital.get("name"));
            assertEquals("ul. Rolna", hospital.get("address"));
            assertEquals("Poznań", hospital.get("city"));
            hospital = (JSONObject) hospitals.get(1);
            assertEquals((long) 2, hospital.get("id"));
            assertEquals("Szpital1", hospital.get("name"));
            assertEquals("ul. Rolna 1", hospital.get("address"));
            assertEquals("Poznań", hospital.get("city"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getHospitalByIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitalById(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Details of hospital with id = 1", response.get("description"));
            JSONObject hospital = (JSONObject) response.get("hospital");
            assertEquals((long) 1, hospital.get("id"));
            assertEquals("Szpital", hospital.get("name"));
            assertEquals("ul. Rolna", hospital.get("address"));
            assertEquals("Poznań", hospital.get("city"));

            response = (JSONObject) parser.parse(restController.getHospitalById(0));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No hospital with id = 0", response.get("description"));

        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getHospitalDirector() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitalsDirectors(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Director of hospital with id = 1", response.get("description"));
            JSONObject director = (JSONObject) response.get("director");
            assertEquals((long) 1, director.get("hospital_id"));
            JSONObject doctor = (JSONObject) director.get("doctor");
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Dyrektor", doctor.get("position"));

            response = (JSONObject) parser.parse(restController.getHospitalsDirectors(0));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No director of hospital with id = 0", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getHospitalsDirectors() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitalsDirectors(-1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all directors", response.get("description"));
            JSONArray directors = (JSONArray) response.get("directors");
            assertEquals(2, directors.size());
            JSONObject director = (JSONObject) directors.get(0);
            assertEquals((long) 1, director.get("hospital_id"));
            JSONObject doctor = (JSONObject) director.get("doctor");
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Dyrektor", doctor.get("position"));

            director = (JSONObject) directors.get(1);
            assertEquals((long) 2, director.get("hospital_id"));
            doctor = (JSONObject) director.get("doctor");
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals((long) 2, doctor.get("hospital_section_id"));
            assertEquals("Dyrektor", doctor.get("position"));

        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getHospitalOrdynatorsTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getHospitalOrdynators(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of ordynators in hospital with id = 1", response.get("description"));
            JSONArray ordynators = (JSONArray) response.get("ordynators");
            assertEquals(2, ordynators.size());
            JSONObject ordynator = (JSONObject) ordynators.get(0);
            assertEquals("Kardiologia", ordynator.get("hospital_section_name"));
            assertEquals((long) 1, ordynator.get("hospital_id"));
            JSONObject doctor = (JSONObject) ordynator.get("doctor");
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Ordynator", doctor.get("position"));
            ordynator = (JSONObject) ordynators.get(1);
            assertEquals("Chirurgia", ordynator.get("hospital_section_name"));
            assertEquals((long) 1, ordynator.get("hospital_id"));
            doctor = (JSONObject) ordynator.get("doctor");
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals((long) 2, doctor.get("hospital_section_id"));
            assertEquals("Ordynator", doctor.get("position"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }
}
