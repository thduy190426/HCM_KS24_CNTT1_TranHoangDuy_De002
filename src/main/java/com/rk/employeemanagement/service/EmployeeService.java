package com.rk.employeemanagement.service;

import com.rk.employeemanagement.model.Employee;
import com.rk.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public void saveEmployee(Employee employee) {
        repository.save(employee);
    }

    public Employee getEmployeeById(String id) {
        return repository.findById(id);
    }

    public void deleteEmployee(String id) {
        repository.deleteById(id);
    }

    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllEmployees();
        }

        String lowerCaseKeyword = keyword.toLowerCase();

        return repository.findAll().stream().filter(emp -> (emp.getFullName() != null && emp.getFullName().toLowerCase().contains(lowerCaseKeyword)) || (emp.getPosition() != null && emp.getPosition().toLowerCase().contains(lowerCaseKeyword))).collect(Collectors.toList());
    }
}