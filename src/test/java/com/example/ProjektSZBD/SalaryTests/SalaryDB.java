package com.example.ProjektSZBD.SalaryTests;

import com.example.ProjektSZBD.Data.Salary;

import java.util.ArrayList;
import java.util.List;

class SalaryDB {

    Salary getSalaryByPosition(String position) {
        return new Salary(position, 5000, 10000);
    }

    public List<Salary> getAllSalaries() {
        ArrayList<Salary> salaries = new ArrayList<>();
        salaries.add(new Salary("Lekarz", 5000, 10000));
        salaries.add(new Salary("Ordynator", 7500, 10000));
        return salaries;
    }
}
