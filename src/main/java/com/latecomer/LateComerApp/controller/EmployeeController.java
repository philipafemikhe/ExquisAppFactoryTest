package com.latecomer.LateComerApp.controller;

import com.latecomer.LateComerApp.entity.Aggregate;
import com.latecomer.LateComerApp.entity.Employee;
import com.latecomer.LateComerApp.service.EmployeeService;
import com.latecomer.LateComerApp.shared.OpeningHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/signin")
    public Employee signIn(@RequestBody  Employee employee){
        return this.employeeService.signin(employee);
    }

    @GetMapping("/all")
    public Object getAllEmployee() throws ParseException {
        List<Employee> allEmployee = this.employeeService.getAll();
        DateFormat hr = new SimpleDateFormat("HH");
        DateFormat mn = new SimpleDateFormat("mm");

        OpeningHour openingHour = new OpeningHour();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now(); //get current date

        String openTime = dtf.format(now) + " " + openingHour.getStartTime(); //append resumption time to current date to get full date/time
        System.out.println("Computed open time: " + openTime);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalTime time = LocalDateTime.parse(openTime, fmt).toLocalTime();
        System.out.println("Opening hour: " + time);

        int openHr = time.getHour() - 1; // we are 1 hour ahead of GMT
        int openMin = time.getMinute();
        allEmployee.sort(Comparator.comparing(Employee::getEmployeeName));
        String temp = "";

        List<Aggregate> aggregates = new ArrayList<>();

        for(Employee e: allEmployee){
            Aggregate tempAgr = new Aggregate();
            LocalTime localTimeIn = e.getTimeIn().toLocalTime();
            System.out.println("Resumed at time: " + (localTimeIn.getHour()-1));
            int lateHrs = localTimeIn.getHour() - openHr - 1; //We are in GMT +1 zone
            int lateMins = localTimeIn.getMinute() - openMin;
            Double owned = 0.2 * lateMins + 60 * lateHrs * 0.2;
            System.out.println("LateHrs: " + lateHrs + " lateMins: " + lateMins + " Owned: " + owned);
            tempAgr.setOwned(owned);
            tempAgr.setEmployee(e);
            aggregates.add(tempAgr);
        }

       Object obj = aggregates.stream()
               .sorted(Comparator.comparingDouble(Aggregate::getOwned).reversed())
                .collect(Collectors.groupingBy(e ->e.getEmployee().getEmployeeName(),
                        Collectors.summingDouble(e -> e.getOwned()
                        )));

        return obj;
    }

    @GetMapping("/employee/{empname}")
    public List<Employee> getEmpRec(@PathVariable String empname){
        return this.employeeService.getEmpRec(empname);
    }

    @DeleteMapping("/employee/delete/{id}") // delete attendance for an employee
    public String deleteEmployeeAttendance(@PathVariable Long id){
        if(this.employeeService.deleteAttendance(id)){
            return "Attendance Record Deleted";
        }
        return "Record not found";
    }

    @PutMapping("/employee/update")
    public Employee updateAttendance(@RequestBody Employee employee){
        return this.employeeService.update(employee);
    }

}
