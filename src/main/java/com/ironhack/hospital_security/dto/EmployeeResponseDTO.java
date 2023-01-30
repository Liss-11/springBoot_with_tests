package com.ironhack.hospital_security.dto;

import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeResponseDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "Department may not be blank")
    private String department;

    @Size(min=3, max=20, message="Name has to contain between 3 and 20 characters")
    private String name;

    @NotNull
    private Status status;

    public EmployeeResponseDTO(Long id, String department, String name, Status status) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.status = status;
    }

    public EmployeeResponseDTO() {
    }

    public static EmployeeResponseDTO fromEmployee(Employee employee){
        var employeeDTO = new EmployeeResponseDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setDepartment(employee.getDepartment());
        employeeDTO.setName(employee.getName());
        employeeDTO.setStatus(employee.getStatus());
        return employeeDTO;
    }
}
