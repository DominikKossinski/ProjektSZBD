package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania choroby.
 */
@Controller
public class AddIllnessController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony dodawania choroby.
     *
     * @return "addIllness" - nazwa widoku strony dodawania choroby
     */
    @GetMapping("/addIllness")
    public String getAddIllness() {
        return "addIllness";
    }
}
