package com.example.ProjektSZBD.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania chorobami.
 */
@Controller
public class ManageIllnessesController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania chorobami.
     *
     * @return "ManageIllnesses" - nazwa widoku strony zarządzania chorobami
     */
    @GetMapping("/manageIllnesses")
    public String getAddIllness() {
        return "ManageIllnesses";
    }
}
