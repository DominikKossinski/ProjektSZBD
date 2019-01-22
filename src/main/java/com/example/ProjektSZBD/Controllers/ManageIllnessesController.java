package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

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
    public String getAddIllness(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "ManageIllnesses";
    }
}
