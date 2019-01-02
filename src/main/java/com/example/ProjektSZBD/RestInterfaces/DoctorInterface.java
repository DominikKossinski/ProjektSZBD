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
    List<Doctor> getDoctorsByHospitalId(long hospitalId);

    /**
     * Metoda zwracająca wszystkie informacje o lekarzach w danym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Doctor) - lista wszystkich lekarzy w danym oddziale
     */
    List<Doctor> getDoctorsByHospitalSectionId(long hospitalSectionId);

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
    List<Doctor> getDoctorsInfoByHospitalId(long hospitalId);

    /**
     * Metoda zwracająca podstawowe informacje o lekarzach (imię, nazwisko, stanowisko) o lekarzach na danym oddziale.
     *
     * @param hospitalSectionId - id oddziału
     * @return (List of Doctor) - lista wszystkich lekarzy na danym oddziale     z podstawowymi informacjami
     */
    List<Doctor> getDoctorsInfoByHospitalSectionId(long hospitalSectionId);

    /**
     * Metoda służąca do dodania lekarza do bazy danych.
     *
     * @param doctor - obiekt reprezentujący lekarza
     * @return (long) - id lekarza, przy pomyślnym zakończeniu op   eracjii,
     * numer błędu, przy błędzie
     */
    long insertDoctor(Doctor doctor);

    /**
     * Metoda służąca do usunięcia lekarza o podanym id.
     *
     * @param id - id lekarza
     * @return (int) - 0 w przypdaku pomyślnego zakończenia usuwania lekarza,
     * kod błędu w przypadku błędu
     */
    int deleteDoctor(long id);

    /**
     * Metoda służąca do aktualizowania pełnych danych lekarza.
     *
     * @param doctor- obiekt reprezentujący lekarza
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int updateDoctor(Doctor doctor);

    /**
     * Metoda służąca do aktualizowania pełnych danych lekarza (bez hasła).
     *
     * @param doctor- obiekt reprezentujący lekarza
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int updateDoctorNoPassword(Doctor doctor);


}
