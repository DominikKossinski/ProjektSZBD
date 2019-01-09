package com.example.ProjektSZBD;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Klasa odpowiadająca za generowanie odpowiedzi serwera.
 */
public class ResponseCreator {

    /**
     * Statyczna metoda klasy odpowiadająca za generowania odpowiedzi w przypadku błędu parsowania do formatu JSON.
     *
     * @param e - błąd parsowania
     * @return (String) - odpowiedź zawierająca informację o błędzie
     */
    public static String parseErrorResponse(ParseException e) {
        e.printStackTrace();
        JSONObject response = new JSONObject();
        response.put("resp_status", "ERROR");
        response.put("description", "JSON PARSE ERROR");
        return response.toJSONString();
    }

    /**
     * Statyczna metoda klasy odpowiadająca za zwrócenie odpowiedzi zawierającej JSONArray.
     *
     * @param key         - klucz, pod którym ma być dostępna lista
     * @param array       - lista, zawierająca dane
     * @param description - krótki opis odpowiedzi
     * @return (String) - tekst w formacie JSON zawierający odpowiedź.
     */
    public static String jsonResponse(String key, JSONArray array, String description) {
        JSONObject response = new JSONObject();
        response.put("resp_status", "ok");
        response.put("description", description);
        response.put(key, array);
        return response.toJSONString();
    }

    /**
     * Statyczna metoda klasy pozwalająca na zwrócenie innego błędu niż błąd parsowania wraz z krótkim opisem.
     *
     * @param description - krótki opis błędu
     * @return (String) - tekst w formacie JSON zawierający odpowiedź.
     */
    public static String jsonErrorResponse(String description) {
        JSONObject response = new JSONObject();
        response.put("resp_status", "ERROR");
        response.put("description", description);
        return response.toJSONString();
    }

    /**
     * Statyczna metoda klasy pozwalająca na przekazanie do odpowiedzi obiektu typu JSONObject.
     *
     * @param key         - klucz, pod którym dostępny będzie obiekt JSON
     * @param object      - obiekt JSON zawierający dane
     * @param description - krótki opis odpowiedzi
     * @return (String) - tekst w formacie JSON zawierający odpowiedź.
     */
    public static String jsonResponse(String key, JSONObject object, String description) {
        JSONObject response = new JSONObject();
        response.put("resp_status", "ok");
        response.put("description", description);
        response.put(key, object);
        return response.toJSONString();
    }

    public static String jsonResponse(String description) {
        JSONObject response = new JSONObject();
        response.put("resp_status", "ok");
        response.put("description", description);
        return response.toJSONString();
    }

}
