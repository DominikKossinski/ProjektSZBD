package com.example.ProjektSZBD.RestControllers;

import com.example.ProjektSZBD.ProjektSzbdApplication;
import com.example.ProjektSZBD.ResponseCreator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestController {


    @RequestMapping(value = "/api/login", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String login(@RequestBody String loginData) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject user = (JSONObject) parser.parse(loginData);
            String password = (String) user.get("password");
            String id = String.valueOf(user.get("id"));
            UserDetails userDetails = ProjektSzbdApplication.getInMemoryUserDetailsManager()
                    .loadUserByUsername(id);
            JSONObject answer = new JSONObject();
            if (userDetails.getPassword().compareTo(password) == 0) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        id, password, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                answer.put("login", authentication.isAuthenticated());
            } else {
                answer.put("login", false);
            }
            return ResponseCreator.jsonResponse("login", answer, "Result of login");
        } catch (ParseException e) {
            return ResponseCreator.parseErrorResponse(e);
        }
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        JSONObject answer = new JSONObject();
        answer.put("logout", true);
        return ResponseCreator.jsonResponse("logout", answer, "Result of logout");
    }
}
