package com.example.ProjektSZBD.Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Director extends Doctor {

    int hospitalId;

    public Director(int hospitalId, int id, String firstName, String lastName, int hospitalSectionId) {
        super(id, firstName, lastName, hospitalSectionId, "Dyrektor");
        this.hospitalId = hospitalId;
    }

    private String toJSONString() throws ParseException {
        return "{ \"hospitalId\":" + hospitalId + "\"doctor\":" + super.toSimpleJSONObject().toJSONString() + "}";
    }

    public JSONObject toJSONObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(this.toJSONString());
    }
}
