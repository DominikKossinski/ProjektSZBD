package com.example.ProjektSZBD.Controllers;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import com.example.ProjektSZBD.RestControllers.HospitalRestController;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controler odpowiadajacy za wyświetlanie strony dodawania lekarza.
 */
@Controller
public class AddDoctorController {

    /**
     * Metoda odpowiedzialna zwracanie widoku strony dodawania lekarza.
     *
     * @param model - model widoku
     * @return "addIllness" - nazwa widoku strony dodawania choroby
     */
    @GetMapping("/addDoctor")
    public String getAddDoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("id", authentication.getName());
        DoctorRestController doctorRestController = new DoctorRestController();
        DoctorInterface doctorInterface = doctorRestController.getDoctorInterface();
        Doctor doctor = doctorInterface.getDoctorsById(Long.parseLong(authentication.getName()));
        HospitalRestController hospitalRestController = new HospitalRestController();
        HospitalInterface hospitalInterface = hospitalRestController.getHospitalInterface();
        Hospital hospital = hospitalInterface.getHospitalByHospitalSectionId(doctor.getHospitalSectionId());
        model.addAttribute("hospitalId", hospital.getId());
        return "addDoctor";
    }
}
