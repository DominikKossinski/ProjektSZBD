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
    List<Prescription> getPrescriptionsByPesel(long pesel);

    /**
     * Metoda zawierająca informacje o receptach wyszukiwanych po id pobytu.
     *
     * @param stayId - id pobytu
     * @return (List of Prescription) - lista recept w danym pobycie.
     */
    List<Prescription> getPrescriptionsByStayId(long stayId);

    /**
     * Metoda zwracająca recepty wystawione przez danego lekarza.
     *
     * @param doctorId - id lekarza
     * @return (List of Prescription) - lista recept wystawionych przez lekarza
     */
    List<Prescription> getPrescriptionsByDoctorId(long doctorId);

    /**
     * Metoda zwracająca recepty wystawione przez danego lekarza danemu pacjentowi.
     *
     * @param doctorId - id lekarza
     * @param pesel    - pesel
     * @return (List of Prescription) - lista recept wystawionych przez lekarza
     */
    List<Prescription> getPrescriptionsByDoctorId(long doctorId, long pesel);

    /**
     * Metoda zwracająca receptę o podanym id.
     *
     * @param id - id recepty
     * @return (Prescription) - recepta o żądanym id
     */
    Prescription getPrescriptionById(long id);


    /**
     * Metoda do obsługi wstawiania recepty.
     *
     * @param prescription - obiekt reprezentujący receptę
     * @return (long) - id recepty w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    long insertPrescription(Prescription prescription);

    /**
     * Metoda do obsługi aktualizowania danych recepty.
     *
     * @param prescription - obiekt reprezentujący receptę
     * @return (long) - 0 w przypadku pomyślnego zakończenia aktualizowania danych recepty
     * kod błędu w przypadku błędu
     */
    int updatePrescription(Prescription prescription);

    /**
     * Metoda do obsługi usuwania recepty.
     *
     * @param id - id recepty
     * @return (long) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int deletePrescription(long id);


}
