package com.ironhack.hospital_security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeDepartmentDTO {

    @NotBlank(message = "Department must not be blank!")
    private String department;

    public EmployeeDepartmentDTO(String department) {
        this.department = department;
    }

    public EmployeeDepartmentDTO() {
    }
}
