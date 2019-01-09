package com.example.ProjektSZBD.IllnessTests;

import com.example.ProjektSZBD.Data.Illness;
import com.example.ProjektSZBD.RestControllers.IllnessRestController;
import com.example.ProjektSZBD.RestInterfaces.IllnessInterface;
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
public class IllnessTests {

    private IllnessDB illnessDB;
    private JSONParser parser;
    private IllnessRestController restController;

    @Before
    public void setUp() {
        parser = new JSONParser();
        illnessDB = new IllnessDB();
        IllnessInterface illnessInterface = new IllnessInterface() {
            @Override
            public List<Illness> getAllIllnesses() {
                return illnessDB.getAllIllnesses();
            }

            @Override
            public Illness getIllnessById(long id) {
                if (id == 0) {
                    return null;
                }
                return illnessDB.getIllnessById(id);
            }

            @Override
            public List<Illness> getAllIllnessesWithPattern(String pattern) {
                return illnessDB.getAllIllnessesWithPattern(pattern);
            }

            @Override
            public long insertIllness(Illness illness) {
                return 0;
            }

            @Override
            public int updateIllness(Illness illness) {
                return 0;
            }

            @Override
            public int deleteIllness(long id) {
                return 0;
            }
        };
        restController = new IllnessRestController(illnessInterface);
    }

    @Test
    public void getAllIllnessesTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getIllnesses(-1, ""));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all illnesses", response.get("description"));
            JSONArray illnesses = (JSONArray) response.get("illnesses");
            assertEquals(2, illnesses.size());
            JSONObject illness = (JSONObject) illnesses.get(0);
            assertEquals((long) 1, illness.get("id"));
            assertEquals("Choroba", illness.get("name"));
            assertEquals("Opis", illness.get("description"));
            illness = (JSONObject) illnesses.get(1);
            assertEquals((long) 2, illness.get("id"));
            assertEquals("Choroba1", illness.get("name"));
            assertEquals("Opis1", illness.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }

    }

    @Test
    public void getIllnessByIdTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getIllnesses(1, ""));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("Illness with id = 1", response.get("description"));
            JSONObject illness = (JSONObject) response.get("illness");
            assertEquals((long) 1, illness.get("id"));
            assertEquals("Choroba", illness.get("name"));
            assertEquals("Opis", illness.get("description"));

            response = (JSONObject) parser.parse(restController.getIllnesses(0, ""));
            assertEquals("ERROR", response.get("resp_status"));
            assertEquals("No illness with id = 0", response.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
            assert (false);
        }

    }

    @Test
    public void getAllIllnessesWithPatternTests() {
        try {
            JSONObject response = (JSONObject) parser.parse(restController.getIllnesses(-1, "choroba"));
            assertEquals("ok", response.get("resp_status"));
            assertEquals("List of all illnesses with pattern = choroba", response.get("description"));
            JSONArray illnesses = (JSONArray) response.get("illnesses");
            assertEquals(2, illnesses.size());
            JSONObject illness = (JSONObject) illnesses.get(0);
            assertEquals((long) 1, illness.get("id"));
            assertEquals("choroba1", illness.get("name"));
            assertEquals("opis", illness.get("description"));
            illness = (JSONObject) illnesses.get(1);
            assertEquals((long) 2, illness.get("id"));
            assertEquals("choroba2", illness.get("name"));
            assertEquals("opis", illness.get("description"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
