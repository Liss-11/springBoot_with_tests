package com.ironhack.hospitals_testscontroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.hospitals_testscontroller.dto.EmployeeDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Employee {

    @Id
    private Long id;

    private String department;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy="employee")
    //@JsonManagedReference
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    public Employee(Long id, String department, String name, Status status) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.status = status;
    }

    public static Employee fromDTO(EmployeeDTO employeeDTO){
        var employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setStatus(employeeDTO.getStatus());
        return employee;
    }

}
