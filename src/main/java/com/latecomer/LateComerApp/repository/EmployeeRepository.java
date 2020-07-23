package com.latecomer.LateComerApp.repository;

import com.latecomer.LateComerApp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmployeeName(String empname);

    void delete(Optional<Employee> attendance);

}
