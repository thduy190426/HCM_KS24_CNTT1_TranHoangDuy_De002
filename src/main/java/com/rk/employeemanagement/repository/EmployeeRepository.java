package com.rk.employeemanagement.repository;

import com.rk.employeemanagement.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EmployeeRepository {
    private final Map<String, Employee> employeeMap = new LinkedHashMap<>();
    private int idCounter = 1;

    public List<Employee> findAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public void save(Employee employee) {
        if (employee.getId() == null || employee.getId().isEmpty()) {
            employee.setId(String.format("NV%03d", idCounter++));
        }
        employeeMap.put(employee.getId(), employee);
    }

    public Employee findById(String id) {
        return employeeMap.get(id);
    }

    public void deleteById(String id) {
        employeeMap.remove(id);
    }
}