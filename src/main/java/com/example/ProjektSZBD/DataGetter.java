package com.example.ProjektSZBD;

import com.example.ProjektSZBD.Data.Doctors.Doctor;
import com.example.ProjektSZBD.Data.Hospital;
import com.example.ProjektSZBD.RestControllers.DoctorRestController;
import com.example.ProjektSZBD.RestControllers.HospitalRestController;
import com.example.ProjektSZBD.RestInterfaces.DoctorInterface;
import com.example.ProjektSZBD.RestInterfaces.HospitalInterface;

public class DataGetter {

    public static long getHospitalId(long doctorId) {
        DoctorRestController doctorRestController = new DoctorRestController();
        DoctorInterface doctorInterface = doctorRestController.getDoctorInterface();
        Doctor doctor = doctorInterface.getDoctorsById(doctorId);
        HospitalRestController hospitalRestController = new HospitalRestController();
        HospitalInterface hospitalInterface = hospitalRestController.getHospitalInterface();
        Hospital hospital = hospitalInterface.getHospitalByHospitalSectionId(doctor.getHospitalSectionId());
        return hospital.getId();
    }
}
