package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HospitalsController {

    @GetMapping("/hospitals")
    public String getHospitals() {

        return "Hospitals";
    }
}
