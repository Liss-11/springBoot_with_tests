package com.ironhack.hospitals_testscontroller.repository;

import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByDateOfBirthBetween(LocalDate from, LocalDate to);

    List<Patient> findByEmployee_Department(String department);

    List<Patient> findByEmployee_Status(Status off);
}
