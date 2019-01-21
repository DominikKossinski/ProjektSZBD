package com.example.ProjektSZBD.Controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.ProjektSZBD.DataGetter.getUserRoles;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania pobytu.
 */
@Controller
public class IllnessesController {

    /**
     * Metoda zwracająca nazwę widoku strony z chorobami.
     *
     * @return "Illnesses" - nazwa strony z chorobami
     */
    @GetMapping("/illnesses")
    public String getIllnesses(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getUserRoles(model, authentication);
        return "Illnesses";
    }
}
