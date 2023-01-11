package com.ironhack.hospitals_testscontroller.dto;

import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.model.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDTO {
    @NotBlank(message = "Name may not be blank")
    @Size(min = 3)
    private String name;

    @Past(message = "It may be previous a today Date")
    private LocalDate dateOfBirth;

    @NotNull
    private Employee employee;

    public static PatientDTO fromPatient(Patient patient) {
        var patientDto = new PatientDTO();
        patientDto.setName(patient.getName());
        patientDto.setDateOfBirth(patient.getDateOfBirth());
        patientDto.setEmployee(patient.getEmployee());
        return patientDto;
    }
}
