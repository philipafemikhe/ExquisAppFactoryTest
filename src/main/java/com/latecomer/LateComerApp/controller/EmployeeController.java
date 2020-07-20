package com.latecomer.LateComerApp.controller;

import com.latecomer.LateComerApp.entity.Employee;
import com.latecomer.LateComerApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/signin")
    public Employee signIn(Employee employee){
        return this.employeeService.signin(employee);
    }
}
