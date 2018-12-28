package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Patient;

import java.util.List;

/**
 * Interfejs wystawiający dane o pacjentach.
 */
public interface PatientInterface {

    /**
     * Metoda zwracająca pełne informacje o pacjencie wyszukiwanym po peselu.
     *
     * @param pesel - pesel
     * @return (Patient) - obiekt zawierający pełne dane o pacjencie
     */
    Patient getPatientByPesel(int pesel);

    /**
     * Metoda zwracająca pełne informacje o wszystkich pacjentach.
     *
     * @return (List of Patient) - lista obiektów zawierających pełne dane o pacjentach
     */
    List<Patient> getAllPatients();

    /**
     * Metoda zwracająca podstawowe dane o pacjencie.
     *
     * @param pesel - pesel
     * @return (Patient) - obiekt zawierający podstawowe dane o pacjencie
     */
    Patient getPatientInfo(int pesel);


    /**
     * Metoda zwracająca podstawowe dane o wszystkich pacjentach.
     *
     * @return (List of Patient) - lista obiektów zawierających pełne dane o pacjentach
     */
    List<Patient> getAllPatientsInfo();
}
