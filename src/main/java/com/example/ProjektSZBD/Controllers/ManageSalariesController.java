package com.example.ProjektSZBD.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony zarządzania płacami.
 */
@Controller
public class ManageSalariesController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony zarządzania płacami.
     *
     * @param model - model widoku
     * @return "addDoctor" - nazwa widoku strony zarządzania płacami
     */
    @GetMapping("/admin/manageSalaries")
    public String getManageSalaries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "ManageSalaries";
    }
}
