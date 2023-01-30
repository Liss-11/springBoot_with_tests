package com.ironhack.hospitals_testscontroller.controller;

import com.ironhack.hospitals_testscontroller.dto.PatientDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.model.Patient;
import com.ironhack.hospitals_testscontroller.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    private List<Patient> getAllPatients(){
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    private Patient getPatientById(@PathVariable Long id){
        return patientService.findById(id);
    }

    @GetMapping("/date_of_birth")
    private List<Patient> getPatientsByDateOfBirthBetween(@RequestParam LocalDate from,
                                                          @RequestParam LocalDate to){
        return patientService.findByDateOfBirthBetween(from, to);
    }

    @GetMapping("/doctor_department/{department}")
    private List<Patient> getPatientsByDoctorDepartment(@PathVariable ("department") String department){
        return patientService.findByEmployeeDepartment(department);
    }

    @GetMapping("/doctors_Status_OFF")
    private List<Patient> getPatientsByDoctorWithStatusOff(){
        return patientService.findByEmployeeStatus(Status.OFF);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient createPatient(@Valid @RequestBody PatientDTO patient){
        return patientService.createPatient(patient);
    }

    //To update all the Patient info

    @PutMapping("/update_all/{id}")
    public Patient updatePatient (@PathVariable Long id, @RequestBody PatientDTO patient){
        return patientService.updatePatient(id, patient);
    }

    //update some patient info

    @PutMapping("/update_some/{id}")
    public Patient updatePatientParams(@PathVariable (name = "id") Long id,
                                       @RequestParam Optional <String> name,
                                       @RequestParam Optional <LocalDate> dateOfBirth,
                                       @RequestParam Optional<Employee> employee){
        return patientService.updatePatientParams(id, name, dateOfBirth, employee);
    }



}
