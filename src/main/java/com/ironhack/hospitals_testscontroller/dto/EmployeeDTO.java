package com.ironhack.hospitals_testscontroller.dto;

import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeDTO {
    @NotNull
    private Long id;

    @NotBlank(message = "Department may not be blank")
    private String department;

    @Size(min=3, max=20, message="Name has to contain between 3 and 20 characters")
    private String name;

    @NotNull
    private Status status;

    public static EmployeeDTO fromEmployee(Employee employee){
        var employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setDepartment(employee.getDepartment());
        employeeDTO.setName(employee.getName());
        employeeDTO.setStatus(employee.getStatus());
        return employeeDTO;
    }
}
