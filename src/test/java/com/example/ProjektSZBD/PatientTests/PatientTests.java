package com.example.ProjektSZBD.PatientTests;

import com.example.ProjektSZBD.Data.Patient;
import com.example.ProjektSZBD.RestControllers.PatientRestController;
import com.example.ProjektSZBD.RestInterfaces.PatientInterface;
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
public class PatientTests {

    private PatientDB patientDB;
    private PatientRestController restController;
    private JSONParser parser;

    @Before
    public void setUp() {
        this.parser = new JSONParser();
        patientDB = new PatientDB();
        PatientInterface patientInterface = new PatientInterface() {
            @Override
            public Patient getPatientByPesel(long pesel) {
                if (pesel == 0) {
                    return null;
                }
                return patientDB.getPatientByPesel(pesel);
            }

            @Override
            public List<Patient> getAllPatients() {
                return patientDB.getAllPatients();
            }

            @Override
            public Patient getPatientInfo(long pesel) {
                if (pesel == 0) {
                    return null;
                }
                return patientDB.getPatientInfoByPesel(pesel);
            }

            @Override
            public List<Patient> getAllPatientsInfo() {
                return patientDB.getAllPatientsInfo();
            }

            @Override
            public int insertPatient(Patient patient) {
                return 0;
            }

            @Override
            public int updatePatient(Patient patient) {
                return 0;
            }

            @Override
            public int deletePatient(long pesel) {
                return 0;
            }
        };
        restController = new PatientRestController(patientInterface);
    }

    @Test
    public void getPatientByPeselTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getPatients(1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Patient with pesel = 1", response.get("description"));
            JSONObject patient = (JSONObject) response.get("patient");
            assertEquals((long) 1, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Kowalski", patient.get("last_name"));
            assertEquals("password", patient.get("password"));

            response = (JSONObject) parser.parse(restController.getPatients(0));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No patient with pesel = 0", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getAllPatientsTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getPatients(-1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all patients", response.get("description"));
            JSONArray patients = (JSONArray) response.get("patients");
            assertEquals(2, patients.size());
            JSONObject patient = (JSONObject) patients.get(0);
            assertEquals((long) 1, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Kowalski", patient.get("last_name"));
            assertEquals("password", patient.get("password"));
            patient = (JSONObject) patients.get(1);
            assertEquals((long) 2, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Nowak", patient.get("last_name"));
            assertEquals("password", patient.get("password"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getPatientInfoTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getPatients(-1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all patients", response.get("description"));
            JSONArray patients = (JSONArray) response.get("patients");
            assertEquals(2, patients.size());
            JSONObject patient = (JSONObject) patients.get(0);
            assertEquals((long) 1, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Kowalski", patient.get("last_name"));
            patient = (JSONObject) patients.get(1);
            assertEquals((long) 2, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Nowak", patient.get("last_name"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    public void getAllPatientsInfoTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getPatientsInfo(-1));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all patients", response.get("description"));
            JSONArray patients = (JSONArray) response.get("patients");
            assertEquals(2, patients.size());
            JSONObject patient = (JSONObject) patients.get(0);
            assertEquals((long) 1, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Kowalski", patient.get("last_name"));
            patient = (JSONObject) patients.get(1);
            assertEquals((long) 2, patient.get("pesel"));
            assertEquals("Jan", patient.get("first_name"));
            assertEquals("Nowak", patient.get("last_name"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }
    }
}
