package com.ironhack.hospital_security.service;

import com.ironhack.hospital_security.dto.PatientDTO;
import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import com.ironhack.hospital_security.model.Patient;
import com.ironhack.hospital_security.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient findById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public List<Patient> findByDateOfBirthBetween(LocalDate from, LocalDate to) {
        return patientRepository.findByDateOfBirthBetween(from, to);
    }

    public List<Patient> findByEmployeeDepartment(String department) {

        return patientRepository.findByEmployee_Department(department);
    }

    public List<Patient> findByEmployeeStatus(Status off) {

        return patientRepository.findByEmployee_Status(off);
    }

    public Patient createPatient(PatientDTO patient) {
        var patientCreated = Patient.fromDTO(patient);
        return patientRepository.save(patientCreated);
    }

    public Patient updatePatient(Long id, PatientDTO patient) {
        var updatePatient = patientRepository.findById(id).orElseThrow();
        updatePatient.setName(patient.getName());
        updatePatient.setDateOfBirth(patient.getDateOfBirth());
        updatePatient.setEmployee(patient.getEmployee());
        return patientRepository.save(updatePatient);
    }

    public Patient updatePatientParams(Long id, Optional<String> name, Optional <LocalDate> dateOfBirth, Optional <Employee> employee) {
        var updatePatient = patientRepository.findById(id).orElseThrow();
        name.ifPresent(updatePatient::setName);
        dateOfBirth.ifPresent(updatePatient::setDateOfBirth);
        employee.ifPresent(updatePatient::setEmployee);
        return patientRepository.save(updatePatient);
    }
}
