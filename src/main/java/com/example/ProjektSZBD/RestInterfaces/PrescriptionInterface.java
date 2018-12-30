package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Prescription;

import java.util.List;

/**
 * Interfejs odpowiedzialny za wystawianie danych o receptach.
 */
public interface PrescriptionInterface {

    /**
     * Metoda zwracająca informacje o receptach wyszukiwanych po peselu pacjenta.
     *
     * @param pesel - pesel
     * @return (List of Prescription) - lista recept danego pacjenta
     */
    List<Prescription> getPrescriptionsByPesel(int pesel);

    /**
     * Metoda zawierająca informacje o receptach wyszukiwanych po id pobytu.
     *
     * @param stayId - id pobytu
     * @return (List of Prescription) - lista recept w danym pobycie.
     */
    List<Prescription> getPrescriptionsByStayId(int stayId);

    /**
     * Metoda zwracająca recepty wystawione przez danego lekarza.
     *
     * @param doctorId - id lekarza
     * @return (List of Prescription) - lista recept wystawionych przez lekarza
     */
    List<Prescription> getPrescriptionsByDoctorId(int doctorId);

    /**
     * Metoda zwracająca recepty wystawione przez danego lekarza danemu pacjentowi.
     *
     * @param doctorId - id lekarza
     * @param pesel    - pesel
     * @return (List of Prescription) - lista recept wystawionych przez lekarza
     */
    List<Prescription> getPrescriptionsByDoctorId(int doctorId, int pesel);

    /**
     * Metoda zwracająca receptę o podanym id.
     *
     * @param id - id recepty
     * @return (Prescription) - recepta o żądanym id
     */
    Prescription getPrescriptionById(int id);


}
