package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ProjektSZBD.ProjektSzbdApplication.getJdbcTemplate;

@RestController
public class HospitalRestController {

    private HospitalInterface hospitalInterface;

    public HospitalRestController() {
        this.hospitalInterface = new HospitalInterface() {
            @Override
            public List<Hospital> getAllHospitals() {
                List<Hospital> hospitals = getJdbcTemplate().query("SELECT * FROM SZPITALE", (rs, arg1) -> {
                    return new Hospital(rs.getInt("id_szpitala"), rs.getString("nazwa_szpitala"),
                            rs.getString("adres"), rs.getString("miasto"));
                });
                return hospitals;
            }

            @Override
            public Hospital getHospitalById(int id) {
                try {
                    Hospital hospital = getJdbcTemplate().queryForObject("SELECT * FROM SZPITALE WHERE ID_SZPITALA = " + id,
                            (rs, ag1) -> {
                                return new Hospital(rs.getInt("id_szpitala"), rs.getString("nazwa_szpitala"),
                                        rs.getString("adres"), rs.getString("miasto"));
                            });
                    return hospital;
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }
        };
    }

    public HospitalRestController(HospitalInterface hospitalInterface) {
        this.hospitalInterface = hospitalInterface;
    }

    @RequestMapping("/api/allHospitals")
    public String getHospitals() {
        JSONArray hospitalsArray = new JSONArray();
        List<Hospital> hospitals = hospitalInterface.getAllHospitals();
        JSONParser parser = new JSONParser();
        for (Hospital hospital : hospitals) {
            try {
                JSONObject hospitalJsonObject = (JSONObject) parser.parse(hospital.toJSONString());
                hospitalsArray.add(hospitalJsonObject);
            } catch (ParseException e) {
                return returnParseErrorResponse(e);
            }
        }
        JSONObject response = new JSONObject();
        response.put("resp_status", "ok");
        response.put("description", "List of all hospitals");
        response.put("hospitals", hospitalsArray);
        return response.toJSONString();
    }

    @RequestMapping("/api/hospital")
    public String getHospitalById(@RequestParam("id") int id) {
        Hospital hospital = hospitalInterface.getHospitalById(id);
        if (hospital == null) {
            JSONObject response = new JSONObject();
            response.put("resp_status", "ERROR");
            response.put("description", "No hospital with id = " + id);
            return response.toJSONString();
        } else {
            try {
                JSONObject response = new JSONObject();
                response.put("resp_status", "ok");
                response.put("description", "Details of hospital with id = " + id);
                response.put("hospital", hospital.toJSONObject());
                return response.toJSONString();
            } catch (ParseException e) {
                return returnParseErrorResponse(e);
            }
        }
    }

    private String returnParseErrorResponse(ParseException e) {
        e.printStackTrace();
        JSONObject response = new JSONObject();
        response.put("resp_status", "ERROR");
        response.put("description", "JSON PARSE ERROR");
        return response.toJSONString();
    }
}
