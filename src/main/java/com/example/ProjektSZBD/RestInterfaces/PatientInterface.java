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
    Patient getPatientByPesel(long pesel);

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
    Patient getPatientInfo(long pesel);


    /**
     * Metoda zwracająca podstawowe dane o wszystkich pacjentach.
     *
     * @return (List of Patient) - lista obiektów zawierających pełne dane o pacjentach
     */
    List<Patient> getAllPatientsInfo();

    /**
     * Metoda do wyszukiwania pacjentów po ciągu znaków.
     *
     * @return (List of Patient) - lista obiektów zawierających pełne dane o pacjentach
     */
    List<Patient> getPatientsByPattern(String pattern);

    /**
     * Metoda obsługująca wstawianie nowego pacjenta.
     *
     * @param patient - obiekt reprezentujący pacjenta
     * @return (int) - 0 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int insertPatient(Patient patient);

    /**
     * Metoda obsługująca aktualizowanie danych pacjenta.
     *
     * @param patient - obiekt reprezentujący pacjenta
     * @return (int) - 0 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int updatePatient(Patient patient);

    /**
     * Metoda obsługująca usuwanie pacjenta.
     *
     * @param pesel - pesel pacjenta
     * @return (int) - 0 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int deletePatient(long pesel);
}
