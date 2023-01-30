package com.ironhack.hospitals_testscontroller.dto;

import com.ironhack.hospitals_testscontroller.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeStatusDTO {
    @NotNull(message = "Status must not be empty!")
    private Status status;

    public EmployeeStatusDTO(Status status) {
        this.status = status;
    }

    public EmployeeStatusDTO() {
    }
}
