package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Illness;

import java.util.List;

/**
 * Interfejs odpowiedzialny za wystawianie danych o chorobach.
 */
public interface IllnessInterface {

    /**
     * Metoda zwracająca listę wszystkich chorób.
     *
     * @return (List of Illness) - lista wszystkich chorób
     */
    List<Illness> getAllIllnesses();

    /**
     * Metoda zwracająca chorobę wyszukiwaną po id.
     *
     * @param id - id choroby
     * @return (Illness) - obiekt zawierający informacje o chorobie
     */
    Illness getIllnessById(long id);

    /**
     * Metoda zwracająca choroby wyszukiwane po ciągu znaków (nazwa choroby musi zawierać ciąg znaków).
     *
     * @return (List of Illness) - lista obiektów zawierających informacje o chorobie
     */
    List<Illness> getAllIllnessesWithPattern(String pattern);

    /**
     * Metoda do obsługi wstawiania choroby.
     *
     * @param illness - obiekt reprezentujący chorobę
     * @return (long) - id choroby w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    long insertIllness(Illness illness);

    /**
     * Metoda do obsługi aktualizowania danych choroby.
     *
     * @param illness - obiekt reprezentujący choroby
     * @return (long) - 0 w przypadku pomyślnego zakończenia aktualizowania danych
     * kod błędu w przypadku błędu
     */
    int updateIllness(Illness illness);

    /**
     * Metoda do obsługi usuwania choroby.
     *
     * @param id - id choroby
     * @return (long) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int deleteIllness(long id);
}
