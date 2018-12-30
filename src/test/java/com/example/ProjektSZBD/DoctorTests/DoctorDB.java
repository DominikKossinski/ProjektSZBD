package com.example.ProjektSZBD.DoctorTests;

import com.example.ProjektSZBD.Data.Doctors.Doctor;

import java.util.ArrayList;
import java.util.List;

class DoctorDB {

    List<Doctor> getAllDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1, "Jan", "Nowak", 5000,
                1, "Lekarz", "password"));
        doctors.add(new Doctor(2, "Jan", "Kowalski", 5000,
                1, "Lekarz", "password"));
        return doctors;
    }

    List<Doctor> getDoctorsByHospitalSectionId(int hospitalSectionId) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1, "Jan", "Nowak", 5000,
                hospitalSectionId, "Lekarz", "password"));
        doctors.add(new Doctor(2, "Jan", "Kowalski", 5000,
                hospitalSectionId, "Lekarz", "password"));
        return doctors;
    }

    List<Doctor> getAllDoctorsInfo() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1, "Jan", "Nowak",
                1, "Lekarz"));
        doctors.add(new Doctor(2, "Jan", "Kowalski",
                1, "Lekarz"));
        return doctors;
    }

    List<Doctor> getDoctorsInfoByHospitalSectionId(int hospitalSectionId) {
        ArrayList<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(1, "Jan", "Nowak",
                hospitalSectionId, "Lekarz"));
        doctors.add(new Doctor(2, "Jan", "Kowalski",
                hospitalSectionId, "Lekarz"));
        return doctors;
    }
}
