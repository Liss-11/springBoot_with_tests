package com.ironhack.hospital_security.utils;

import com.ironhack.hospital_security.enums.Role;
import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import com.ironhack.hospital_security.model.Patient;
import com.ironhack.hospital_security.model.User;
import com.ironhack.hospital_security.repository.EmployeeRepository;
import com.ironhack.hospital_security.repository.PatientRepository;
import com.ironhack.hospital_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Log
@Profile("demo")
@RequiredArgsConstructor
public class DataLoader {
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataHospital(){

        var employees = List.of(new Employee(356712L, "cardiology", "Alonso Flores", Status.ON_CALL),
                new Employee(564134L, "immunology", "Sam Ortega", Status.ON),
                new Employee (761527L, "cardiology", "German Ruiz", Status.OFF),
                new Employee(166552L, "pulmonary", "Maria Lin", Status.ON),
                new Employee (156545L, "orthopaedic", "Paolo Rodriguez", Status.ON_CALL),
                new Employee(172456L, "psychiatric", "John Paul Armes", Status.OFF));

        employeeRepository.saveAll(employees);

        var patients = List.of(new Patient("Jaime Jordan", LocalDate.parse("1984-03-02"), employees.get(1)),
                new Patient("Marian Garcia", LocalDate.parse("1972-01-12"), employees.get(1)),
                new Patient("Julia Dusterdieck", LocalDate.parse("1954-06-11"), employees.get(0)),
                new Patient("Steve McDuck", LocalDate.parse("1931-11-10"), employees.get(2)),
                new Patient("Marian Garcia", LocalDate.parse("1999-02-15"), employees.get(5)));

        patientRepository.saveAll(patients);

        var users = (List.of(new User("alissia", passwordEncoder.encode("alissia"), Role.ADMIN),
                new User("ara", passwordEncoder.encode("ara"), Role.CONTRIBUTOR)));
        userRepository.saveAll(users);

    }

}
