package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Doctors.Doctor;

import java.util.List;

/**
 * Interfejs odpowiedzialny za wystawianie danych lekarzy.
 */
public interface DoctorInterface {

    /**
     * Metoda zwracająca wszystkie dane o wszystkich lekarzach.
     *
     * @return (List of Doctor) - lista wszystkich lekarzy
     */
    List<Doctor> getAllDoctors();

    /**
     * Metoda zwracająca wszystkie informacje o lekarzach w danym szpitalu.
     *
     * @param hospitalId - id szpitala
     * @return (List of Doctor) - lista wszystkich lekarzy w danym szpitalu
     */
    List<Doctor> getDoctorsByHospitalId(int hospitalId);

    /**
     * Metoda zwracająca wszystkie informacje o lekarzach w danym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Doctor) - lista wszystkich lekarzy w danym oddziale
     */
    List<Doctor> getDoctorsByHospitalSectionId(int hospitalSectionId);

    /**
     * Metoda zwracająca podstawowe informacje o lekarzach (imię, nazwisko, stanowisko) o wszystkich lekarzach.
     *
     * @return (List of Doctor) - lista wszystkich lekarzy z podstawowymi informacjami
     */
    List<Doctor> getAllDoctorsInfo();

    /**
     * Metoda zwracająca podstawowe informacje o lekarzach (imię, nazwisko, stanowisko) o lekarzach w danym szpitalu.
     *
     * @param hospitalId - id szpitala
     * @return (List of Doctor) - lista wszystkich lekarzy w danym szpitalu z podstawowymi informacjami
     */
    List<Doctor> getDoctorsInfoByHospitalId(int hospitalId);

    /**
     * Metoda zwracająca podstawowe informacje o lekarzach (imię, nazwisko, stanowisko) o lekarzach na danym oddziale/
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Doctor) - lista wszystkich lekarzy na danym oddziale     z podstawowymi informacjami
     */
    List<Doctor> getDoctorsInfoByHospitalSectionId(int hospitalSectionId);
}
