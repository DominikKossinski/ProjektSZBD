package com.example.ProjektSZBD.RestInterfaces;

import com.example.ProjektSZBD.Data.Salary;

import java.util.List;

/**
 * Interfejs odpowiadający za wystawienie danych o płacach.
 */
public interface SalaryInterface {

    /**
     * Metoda zwracająca płacę w zależności od stanowiska.
     *
     * @param position - stanowisko
     * @return (Salary) - obiekt reprezentujący płacę
     */
    Salary getSalaryByPosition(String position);

    /**
     * Metoda zwracająca informacje o wszystkich płacach.
     *
     * @return (List of Salary) - obiekty reprezentujące płace
     */
    List<Salary> getAllSalaries();
}
