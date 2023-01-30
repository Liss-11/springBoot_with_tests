package com.ironhack.hospital_security.dto;

import com.ironhack.hospital_security.enums.Status;
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
