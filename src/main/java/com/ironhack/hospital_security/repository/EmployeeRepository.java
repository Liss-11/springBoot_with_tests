package com.ironhack.hospital_security.repository;

import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByStatus(Status status);

    List<Employee> findByDepartment(String department);
}
