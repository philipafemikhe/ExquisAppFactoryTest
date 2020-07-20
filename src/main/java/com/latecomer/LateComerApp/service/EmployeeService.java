package com.latecomer.LateComerApp.service;

import com.latecomer.LateComerApp.entity.Employee;
import com.latecomer.LateComerApp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;


    public Employee signin(Employee employee) {
        return this.employeeRepository.save(employee);
    }
}
