package com.example.ProjektSZBD.PatientTests;

import com.example.ProjektSZBD.Data.Patient;

import java.util.ArrayList;
import java.util.List;

class PatientDB {

    Patient getPatientByPesel(long pesel) {
        return new Patient(pesel, "Jan", "Kowalski", "password");
    }

    List<Patient> getAllPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(new Patient(1, "Jan", "Kowalski", "password"));
        patients.add(new Patient(2, "Jan", "Nowak", "password"));
        return patients;
    }

    Patient getPatientInfoByPesel(long pesel) {
        return new Patient(1, "Jan", "Kowalski");
    }

    List<Patient> getAllPatientsInfo() {
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(new Patient(1, "Jan", "Kowalski"));
        patients.add(new Patient(2, "Jan", "Nowak"));
        return patients;
    }
}
