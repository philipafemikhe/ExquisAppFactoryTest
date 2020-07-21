package com.latecomer.LateComerApp.controller;

import com.latecomer.LateComerApp.entity.Aggregate;
import com.latecomer.LateComerApp.entity.Employee;
import com.latecomer.LateComerApp.service.EmployeeService;
import com.latecomer.LateComerApp.shared.OpeningHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    public List<Aggregate> getAllEmployee(){
        List<Employee> allEmployee = this.employeeService.getAll();
        Aggregate summary = new Aggregate();
        SimpleDateFormat hr = new SimpleDateFormat("HH");
        SimpleDateFormat mn = new SimpleDateFormat("mm");


        Time openTime = OpeningHour.startTime;
        int openHr = Integer.parseInt(hr.format(openTime));
        int openMin = Integer.parseInt(hr.format(openTime));

        allEmployee.sort(Comparator.comparing(Employee::getEmployeeName));
        String temp = "";

        List<Aggregate> aggregates = null;
        Aggregate tempAgr = new Aggregate();
        for(Employee e: allEmployee){
           if(!e.getEmployeeName().equals(temp)){
               if(tempAgr != null) aggregates.add(tempAgr);
               int lateHrs = Integer.parseInt(hr.format(e.getTimeIn().toString())) - openHr;
               int lateMins = Integer.parseInt(mn.format(e.getTimeIn().toString())) - openMin;
               Double owned = 0.2 * lateMins + 60 * lateHrs * 0.2;
//               agr.setOwned(owned);
//               aggregates.add(agr);
               tempAgr.setOwned(owned);
               tempAgr.setEmployee(e);
           }else{
               int lateHrs = Integer.parseInt(hr.format(e.getTimeIn().toString())) - openHr;
               int lateMins = Integer.parseInt(mn.format(e.getTimeIn().toString())) - openMin;
               Double owned = 0.2 * lateMins + 60 * lateHrs * 0.2;
               tempAgr.setOwned(tempAgr.getOwned() + owned);
           }
        }

        return aggregates;

//        allEmployee.stream()
//                .collect(Collectors.groupingBy(e -> e.getEmployeeName(),
//                        Collectors.summingInt(e->
//                        {
//                            int lateHrs = Integer.parseInt(hr.format(e.getTimeIn().toString())) - openHr;
//                            int lateMins = Integer.parseInt(mn.format(e.getTimeIn().toString())) - openMin;
//                            Double owned = 0.2 * lateMins + 60 * lateHrs * 0.2;
//                            System.out.println("Owned: " + owned);
//                            return;
//                        })))
//                .forEach((id,sumTargetCost)->System.out.println(id+"\t"+sumTargetCost));
//        return  allEmployee;
    }

    @GetMapping("/employee/{empname}")
    public List<Employee> getEmpRec(@PathVariable String empname){
        return this.employeeService.getEmpRec(empname);
    }
}
