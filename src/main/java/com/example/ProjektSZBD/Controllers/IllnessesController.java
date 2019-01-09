package com.example.ProjektSZBD.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania pobytu.
 */
@Controller
public class IllnessesController {

    /**
     * Metoda zwracająca nazwę widoku strony z chorobami.
     *
     * @return "illnesses" - nazwa strony z chorobami
     */
    @GetMapping("/illnesses")
    public String getIllnesses() {
        return "illnesses";
    }
}
