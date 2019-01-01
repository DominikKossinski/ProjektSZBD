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
}
