package com.example.ProjektSZBD.DoctorTests;


import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
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
public class DoctorTests {

    private DoctorInterface doctorInterface;
    private DoctorDB doctorDB;
    private DoctorRestController restController;
    private JSONParser parser;

    @Before
    public void setUp() {
        this.parser = new JSONParser();
        this.doctorDB = new DoctorDB();
        this.doctorInterface = new DoctorInterface() {
            @Override
            public List<Doctor> getAllDoctors() {
                return doctorDB.getAllDoctors();
            }

            @Override
            public List<Doctor> getDoctorsByHospitalId(int hospitalId) {
                return doctorDB.getAllDoctors();
            }

            @Override
            public List<Doctor> getDoctorsByHospitalSectionId(int hospitalSectionId) {
                return doctorDB.getDoctorsByHospitalSectionId(hospitalSectionId);
            }

            @Override
            public List<Doctor> getAllDoctorsInfo() {
                return doctorDB.getAllDoctorsInfo();
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalId(int hospitalId) {
                return doctorDB.getAllDoctorsInfo();
            }

            @Override
            public List<Doctor> getDoctorsInfoByHospitalSectionId(int hospitalSectionId) {
                return doctorDB.getDoctorsInfoByHospitalSectionId(hospitalSectionId);
            }
        };
        this.restController = new DoctorRestController(doctorInterface);
    }

    @Test
    public void getAllDoctorsTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctors(-1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all doctors", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getDoctorsByHospitalIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctors(-1, 1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of doctors from hospital with id = 1", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getDoctorsByHospitalSectionIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctors(1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of doctors from hospital section with id = 1", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals(5000.0, doctor.get("salary"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            assertEquals("password", doctor.get("password"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getAllDoctorsInfoTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctorsInfo(-1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all doctors info", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getDoctorsInfoByHospitalIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctorsInfo(-1, 1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of doctors info from hospital with id = 1", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getDoctorsInfoByHospitalSectionIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getAllDoctorsInfo(1, -1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of doctors info from hospital section with id = 1", response.get("description"));
            JSONArray doctors = (JSONArray) response.get("doctors");
            assertEquals(2, doctors.size());
            JSONObject doctor = (JSONObject) doctors.get(0);
            assertEquals((long) 1, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Nowak", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
            doctor = (JSONObject) doctors.get(1);
            assertEquals((long) 2, doctor.get("id"));
            assertEquals("Jan", doctor.get("first_name"));
            assertEquals("Kowalski", doctor.get("last_name"));
            assertEquals((long) 1, doctor.get("hospital_section_id"));
            assertEquals("Lekarz", doctor.get("position"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }
}
