package com.ironhack.hospitals_testscontroller.model;

import com.ironhack.hospitals_testscontroller.dto.PatientDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name="admitted_by")
    //  @JsonBackReference
    private Employee employee;

    public Patient(String name, LocalDate dateOfBirth, Employee employee) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.employee = employee;
    }

    public static Patient fromDTO(PatientDTO patientDTO){
        var patient = new Patient();
        patient.setName(patientDTO.getName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setEmployee(patientDTO.getEmployee());
        return patient;
    }
}
