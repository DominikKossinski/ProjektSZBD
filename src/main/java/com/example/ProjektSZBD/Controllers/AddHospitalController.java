package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddHospitalController {

    @GetMapping("/admin/addHospital")
    public String addHospital() {
        return "addHospital";
    }
}
