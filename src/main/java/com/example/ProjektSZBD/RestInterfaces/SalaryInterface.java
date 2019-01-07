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

    /**
     * Metoda do obsługi wstawiania płacy.
     *
     * @param salary - obiekt reprezentujący płacę
     * @return (int) - 1 w przypadku pomyślnego zakończenia dodawania
     * kod błędu w przypadku błędu
     */
    int insertSalary(Salary salary);

    /**
     * Metoda do obsługi aktualizowania danych płacy.
     *
     * @param salary - obiekt reprezentujący płacę
     * @return (int) - 0 w przypadku pomyślnego zakończenia aktualizowania danych płacy
     * kod błędu w przypadku błędu
     */
    int updateSalary(Salary salary);

    /**
     * Metoda do obsługi usuwania płacy.
     *
     * @param position - nazwa stanowiska
     * @return (int) - 0 w przypadku pomyślnego zakończenia usuwania
     * kod błędu w przypadku błędu
     */
    int deleteSalary(String position);
}
